package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
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
        testLobby.setLobbyOwner(1L); // Assuming the owner is identified by a user ID
        testLobby.setLobbyJoinCode(123456L);

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
        when(lobbyRepository.findByJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        Lobby foundLobby = lobbyService.findLobbyByJoinCode(testLobby.getLobbyJoinCode());

        assertNotNull(foundLobby);
        assertEquals(testLobby.getLobbyId(), foundLobby.getLobbyId());
    }

    @Test
    public void findLobbyByJoinCode_nonExistingCode_throwsException() {
        Long nonExistingCode = 654321L;
        when(lobbyRepository.findByJoinCode(nonExistingCode)).thenReturn(Optional.empty());

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

        lobbyService.deleteLobby(testLobby.getLobbyId());

        verify(lobbyRepository, times(1)).delete(testLobby);
    }
}

