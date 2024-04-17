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

    //TODO check if the userready really was updated to true (GS)
    @Test
    public void checkIfPlayersAreReady_allPlayersReady_returnsTrue() {
        // Create a user object with userReady set to true
        User readyUser = new User();
        readyUser.setUserReady(true);
    
        // Add players to the testLobby
        testLobby.addPlayer(1L);
        testLobby.addPlayer(2L);
        testLobby.addPlayer(3L);
        testLobby.addPlayer(4L);
        testLobby.addPlayer(5L);
        testLobby.addPlayer(6L);
        testLobby.addPlayer(7L);
        testLobby.addPlayer(8L);
    
        // Configure userRepository mock to return a readyUser for each findById call
        when(userRepository.findById(1L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(4L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(5L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(6L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(7L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(8L)).thenReturn(Optional.of(readyUser));
    
        // Call the method under test
        boolean result = lobbyService.checkIfPlayersAreReady(testLobby);
    
        // Assert that the result is true (all players are ready)
        assertTrue(result);
    }

    @Test
    public void checkIfPlayersAreReady_notAllPlayersReady_returnsFalse() {
        // Create a user object with userReady set to true
        User readyUser = new User();
        readyUser.setUserReady(true);
    
        // Create a user object with userReady set to false
        User notReadyUser = new User();
        notReadyUser.setUserReady(false);
    
        // Add players to the testLobby
        testLobby.addPlayer(1L);
        testLobby.addPlayer(2L);
        testLobby.addPlayer(3L);
        testLobby.addPlayer(4L);
        testLobby.addPlayer(5L);
        testLobby.addPlayer(6L);
        testLobby.addPlayer(7L);
        testLobby.addPlayer(8L);
    
        // Configure userRepository mock to return a readyUser for each findById call
        when(userRepository.findById(1L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(4L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(5L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(6L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(7L)).thenReturn(Optional.of(readyUser));
        when(userRepository.findById(8L)).thenReturn(Optional.of(notReadyUser));
    
        // Call the method under test
        boolean result = lobbyService.checkIfPlayersAreReady(testLobby);
    
        // Assert that the result is false (not all players are ready)
        assertFalse(result);
    }

    @Test
    public void checkIfPlayersAreReady_nonExistingUser_throwsException() {
        // Add players to the testLobby
        testLobby.addPlayer(1L);
        testLobby.addPlayer(2L);
        testLobby.addPlayer(3L);
        testLobby.addPlayer(4L);
        testLobby.addPlayer(5L);
        testLobby.addPlayer(6L);
        testLobby.addPlayer(7L);
        testLobby.addPlayer(8L);
    
        // Configure userRepository mock to return null for each findById call
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(userRepository.findById(4L)).thenReturn(Optional.empty());
        when(userRepository.findById(5L)).thenReturn(Optional.empty());
        when(userRepository.findById(6L)).thenReturn(Optional.empty());
        when(userRepository.findById(7L)).thenReturn(Optional.empty());
        when(userRepository.findById(8L)).thenReturn(Optional.empty());
    
        // Call the method under test and assert that a ResponseStatusException is thrown
        assertThrows(ResponseStatusException.class, () -> lobbyService.checkIfPlayersAreReady(testLobby));
    }

    @Test
    public void checkIfLeaveLobby_existingUser_leavesLobby() {
        Long userId = 1L;
        testLobby.addPlayer(userId);
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));

        lobbyService.leaveLobby(userId, testLobby.getLobbyId());

        assertFalse(testLobby.getPlayers().contains(userId));
    }

    @Test
    public void checkIfLeaveLobby_nonExistingUser_throwsException() {
        Long nonExistingUser = 200L;
        when(lobbyRepository.findByLobbyJoinCode(testLobby.getLobbyJoinCode())).thenReturn(Optional.of(testLobby));
        when(userRepository.existsById(nonExistingUser)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> lobbyService.leaveLobby(nonExistingUser, testLobby.getLobbyId()));
    }
}