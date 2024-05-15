package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setUserReady(false);
    }

    @Test
    public void setUserReady_UserExists_UpdatesReadyStatus() {
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        userService.setUserReady(testUser.getUserId());

        assertTrue(testUser.getUserReady(), "User readiness should be set to true");
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(testUser);
    }

    @Test
    public void setUserReady_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 2L;
        Mockito.when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.setUserReady(nonExistentUserId),
            "Should throw ResponseStatusException when user is not found");
    }

    @Test
    public void createUser_validInputs_success() {
        Mockito.when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser("newUsername", false);

        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class));
        assertNotNull(createdUser, "The created user should not be null.");
        assertEquals("newUsername", createdUser.getUsername(), "Username should match the input.");
        assertNotNull(createdUser.getProfilePicture(), "Profile picture should be set.");
        assertFalse(createdUser.getLobbyOwner(), "Lobby owner should be false.");
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        assertThrows(ResponseStatusException.class, () -> userService.createUser("testUsername", false));
    }

    @Test
    public void getUser_existingUser_success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUser(1L);

        assertEquals(testUser.getUserId(), foundUser.getUserId());
    }

    @Test
    public void getUser_nonExistingUser_throwsException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUser(1L));
    }

    @Test
    public void getUsers_success() {
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        anotherUser.setUsername("anotherUser");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, anotherUser));

        List<User> users = userService.getUsers();

        assertEquals(2, users.size(), "There should be two users in the list.");
        assertEquals("testUsername", users.get(0).getUsername(), "First user's username should be 'testUsername'.");
        assertEquals("anotherUser", users.get(1).getUsername(), "Second user's username should be 'anotherUser'.");
    }

    @Test
    public void updateProfilePicture_UserExists_UpdatesProfilePicture() {
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        userService.updateProfilePicture(testUser.getUserId());

        assertNotNull(testUser.getProfilePicture(), "Profile picture should not be null.");
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    }

    @Test
    public void updateProfilePicture_UserNotFound_ThrowsException() {
        Long nonExistentUserId = 2L;
        Mockito.when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.updateProfilePicture(nonExistentUserId),
            "Should throw ResponseStatusException when user is not found");
    }

    @Test
    public void deleteUser_existingUser_success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
    }

    @Test
    public void deleteUser_nonExistingUser_throwsException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void deleteUser_userInLobby_lobbyUpdated() {
        // Arrange
        testUser.setLobbyId(1L);
        testUser.setLobbyOwner(true);
        Lobby testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>(Arrays.asList(testUser.getUserId())));
        testLobby.setLobbyOwner(testUser.getUserId());
        testLobby.setGameActive(true);
    
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(lobbyRepository.findById(1L)).thenReturn(Optional.of(testLobby));
    
        // Act
        userService.deleteUser(1L);
    
        // Assert
        assertFalse(testLobby.getPlayers().contains(testUser.getUserId()), "Lobby should no longer contain the deleted user.");
        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
    }
    
    @Test
    public void deleteUser_userInLobby_newOwnerAssigned() {
        // Arrange
        testUser.setLobbyId(1L);
        testUser.setLobbyOwner(true);
        User anotherUser = new User();
        anotherUser.setUserId(2L);
        Lobby testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>(Arrays.asList(testUser.getUserId(), anotherUser.getUserId())));
        testLobby.setLobbyOwner(testUser.getUserId());
    
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(anotherUser));
        Mockito.when(lobbyRepository.findById(1L)).thenReturn(Optional.of(testLobby));
    
        // Act
        userService.deleteUser(1L);
    
        // Assert
        assertFalse(testLobby.getPlayers().contains(testUser.getUserId()), "Lobby should no longer contain the deleted user.");
        assertEquals(2L, testLobby.getLobbyOwner(), "Another user should be assigned as the new lobby owner.");
        Mockito.verify(userRepository, Mockito.times(1)).save(anotherUser);
        Mockito.verify(lobbyRepository, Mockito.times(1)).saveAndFlush(testLobby);
        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
    }
    
    @Test
    public void deleteUser_userInLobby_lobbyDeletedWhenEmpty() {
        // Arrange
        testUser.setLobbyId(1L);
        testUser.setLobbyOwner(true);
        Lobby testLobby = new Lobby();
        testLobby.setLobbyId(1L);
        testLobby.setPlayers(new ArrayList<>(Arrays.asList(testUser.getUserId())));
        testLobby.setLobbyOwner(testUser.getUserId());
    
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(lobbyRepository.findById(1L)).thenReturn(Optional.of(testLobby));
    
        // Act
        userService.deleteUser(1L);
    
        // Assert
        assertTrue(testLobby.getPlayers().isEmpty(), "Lobby should be empty after user deletion.");
        Mockito.verify(lobbyRepository, Mockito.times(1)).delete(testLobby);
        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
    }

    @Test
    public void testSetAndGetLobbyId() {
        User user = new User();
        Long expectedLobbyId = 100L;

        user.setLobbyId(expectedLobbyId);
        Long actualLobbyId = user.getLobbyId();

        assertEquals(expectedLobbyId, actualLobbyId, "The getter should return the value that was set by the setter.");
    }

}
