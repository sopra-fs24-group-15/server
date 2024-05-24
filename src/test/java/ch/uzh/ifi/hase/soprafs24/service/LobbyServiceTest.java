package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setLobbyOwner(1L); 
        testLobby.setLobbyJoinCode("123456");

        user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        

        when(lobbyRepository.save(any(Lobby.class))).thenReturn(testLobby);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);
    }

    @Test
    public void checkGetLobby_existingLobby_success() {
        // when
        Lobby createdLobby = lobbyService.createLobby(1L);

        // then
        assertEquals(createdLobby.getLobbyId(), testLobby.getLobbyId());
        assertEquals(createdLobby.getLobbyOwner(), testLobby.getLobbyOwner());
        assertEquals(createdLobby.getLobbyJoinCode(), testLobby.getLobbyJoinCode());
    }

    @Test
    public void checkGetLobby_nonExistingLobby_throwsException() {
        // given
        when(lobbyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(1L, 1L));
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
    public void createLobby_validInputs_success() {
        Long userId = 1L;

        Lobby createdLobby = lobbyService.createLobby(userId);

        assertNotNull(createdLobby);
        assertEquals(testLobby.getLobbyId(), createdLobby.getLobbyId());
        assertEquals(testLobby.getLobbyOwner(), createdLobby.getLobbyOwner());
        verify(lobbyRepository, times(1)).save(any(Lobby.class));
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
        when(userRepository.findById(testLobby.getLobbyOwner())).thenReturn(Optional.of(new User()));

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

        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(testLobby.getLobbyId(), nonOwner));
    }

    @Test
    public void deleteLobby_nonExistingUser_throwsException() {
        Long nonExistingUser = 2L;
        when(lobbyRepository.findById(testLobby.getLobbyId())).thenReturn(Optional.of(testLobby));
        when(userRepository.existsById(nonExistingUser)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> lobbyService.deleteLobby(testLobby.getLobbyId(), nonExistingUser));
    }

    

    @Test
    public void joinLobby_fullLobby_throwsException() {
        Long userId = 2L;
        for (int i = 1; i <= 8; i++) {
            testLobby.addPlayer((long) i);
        }
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(userId, testLobby));
    }

    @Test
    public void joinLobby_nonExistingUser_throwsException() {
        Long nonExistingUser = 200L;
        when(userRepository.findById(nonExistingUser)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(nonExistingUser, testLobby));
    }

    @Test
    public void joinLobby_gameAlreadyActive_throwsException() {
        Long userId = 2L;
        testLobby.setGameActive(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(userId, testLobby));
    }

    @Test
    public void checkIfPlayersAreReady_allPlayersReady_returnsTrue() {
        User readyUser = new User();
        readyUser.setUserReady(true);

        for (long i = 1; i <= 8; i++) {
            testLobby.addPlayer(i);
            when(userRepository.findById(i)).thenReturn(Optional.of(readyUser));
        }

        boolean result = lobbyService.checkIfPlayersAreReady(testLobby);

        assertTrue(result);
    }

    @Test
    public void checkIfPlayersAreReady_notAllPlayersReady_returnsFalse() {
        User readyUser = new User();
        readyUser.setUserReady(true);

        User notReadyUser = new User();
        notReadyUser.setUserReady(false);

        for (long i = 1; i <= 7; i++) {
            testLobby.addPlayer(i);
            when(userRepository.findById(i)).thenReturn(Optional.of(readyUser));
        }
        testLobby.addPlayer(8L);
        when(userRepository.findById(8L)).thenReturn(Optional.of(notReadyUser));

        boolean result = lobbyService.checkIfPlayersAreReady(testLobby);

        assertFalse(result);
    }

    @Test
    public void checkIfPlayersAreReady_nonExistingUser_throwsException() {
        for (long i = 1; i <= 8; i++) {
            testLobby.addPlayer(i);
            when(userRepository.findById(i)).thenReturn(Optional.empty());
        }

        assertThrows(ResponseStatusException.class, () -> lobbyService.checkIfPlayersAreReady(testLobby));
    }

    

   

    
}
