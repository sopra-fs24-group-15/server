package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyOwner(1L); 
        testLobby.setLobbyJoinCode("123456");

        when(lobbyRepository.save(any(Lobby.class))).thenReturn(testLobby);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
    }

    @Test
    public void createLobby_validInputs_success() {
        Long userId = 1L;

        Lobby createdLobby = lobbyService.createLobby(userId);

        assertNotNull(createdLobby);
        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testLobby.getLobbyOwner(), createdLobby.getLobbyOwner());
        verify(lobbyRepository, times(1)).save(any(Lobby.class));
    }

    @Test
    public void findLobbyByJoinCode_existingCode_success() {
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        Lobby foundLobby = lobbyService.findLobbyByJoinCode(testLobby.getLobbyJoinCode());

        assertNotNull(foundLobby);
        assertEquals(testLobby.getLobbyId(), foundLobby.getLobbyId());
    }

    @Test
    public void findLobbyByJoinCode_nonExistingCode_throwsException() {
        String nonExistingCode = "654321";
        when(lobbyRepository.findByLobbyJoinCode(nonExistingCode)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.findLobbyByJoinCode(nonExistingCode));
    }

    @Test
    public void updateLobbyOwner_existingLobbyAndUser_updatesOwner() {
        Long newOwnerId = 2L;
        when(lobbyRepository.findById(testLobby.getLobbyId())).thenReturn(Optional.of(testLobby));

        lobbyService.updateLobbyOwner(newOwnerId, testLobby.getLobbyId());

        assertEquals(newOwnerId, testLobby.getLobbyOwner());
    }

    @Test
    public void deleteLobby_existingLobby_deletesLobby() {
        when(lobbyRepository.findById(testLobby.getLobbyId())).thenReturn(Optional.of(testLobby));

        lobbyService.deleteLobby(testLobby.getLobbyId(), testLobby.getLobbyOwner());

        verify(lobbyRepository, times(1)).delete(testLobby);
    }

    @Test
    public void deleteLobby_nonExistingLobby_throwsException() {
        Long nonExistingLobbyId = 2L;
        when(lobbyRepository.findById(nonExistingLobbyId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(nonExistingLobbyId, testLobby.getLobbyOwner()));
    }

    @Test
    public void deleteLobby_nonOwner_throwsException() {
        Long nonOwner = 2L;
        when(lobbyRepository.findById(testLobby.getLobbyId())).thenReturn(Optional.of(testLobby));

        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(testLobby.getLobbyId(), nonOwner
        ));
    }

    @Test
    public void deleteLobby_nonExistingUser_throwsException() {
        Long nonExistingUser = 2L;
        when(lobbyRepository.findById(testLobby.getLobbyId())).thenReturn(Optional.of(testLobby));
        when(userRepository.existsById(nonExistingUser)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(testLobby.getLobbyId(), nonExistingUser));
    }

    @Test
    public void joinLobby_existingLobbyAndUser_joinsLobby() {
        Long userId = 2L;
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        lobbyService.joinLobby(userId, testLobby);

        assertEquals(testLobby.getPlayers().contains(userId), true);
    }

    @Test
    public void joinLobby_fullLobby_throwsException() {
        Long userId = 2L;
        testLobby.addPlayer(2L);
        testLobby.addPlayer(3L);
        testLobby.addPlayer(4L);
        testLobby.addPlayer(5L);
        testLobby.addPlayer(6L);
        testLobby.addPlayer(7L);
        testLobby.addPlayer(8L);
        testLobby.addPlayer(9L);
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(userId, testLobby));
    }

    @Test
    public void joinLobby_nonExistingUser_throwsException() {
        Long nonExistingUser = 200L;
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));
        when(userRepository.existsById(nonExistingUser)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(nonExistingUser, testLobby));
    }

    @Test
    public void joinLobby_nonExistingLobby_throwsException() {
        Long userId = 2L;
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(userId, testLobby));
    }
}