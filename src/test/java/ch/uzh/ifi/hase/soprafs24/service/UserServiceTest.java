package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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
        // Arrange
        Mockito.when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // Act
        userService.setUserReady(testUser.getUserId());

        // Assert
        assertTrue(testUser.getUserReady(), "User readiness should be set to true");
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(testUser); // Verify that the user is saved after updating
    }

    @Test
    public void setUserReady_UserNotFound_ThrowsException() {
        // Arrange
        Long nonExistentUserId = 2L; // Assuming 2L is a non-existent user ID
        Mockito.when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> userService.setUserReady(nonExistentUserId),
            "Should throw ResponseStatusException when user is not found");
    }

    @Test
    public void createUser_validInputs_success() {
        // Mock setup
        Mockito.when(userRepository.findByUsername("newUsername")).thenReturn(Optional.empty());
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User createdUser = userService.createUser("newUsername", false);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any(User.class)); // Checks if saveAndFlush was called
        assertNotNull(createdUser, "The created user should not be null.");
        assertEquals("newUsername", createdUser.getUsername(), "Username should match the input.");
        assertEquals("testProfilePicture", createdUser.getProfilePicture(), "Profile picture should match the input.");
        assertFalse(createdUser.getLobbyOwner(), "Lobby owner should be false.");
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        // then -> attempt to create second user with same username -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser("testUsername", false));
    }

    @Test
    public void getUser_existingUser_success() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        User foundUser = userService.getUser(1L);

        // then
        assertEquals(testUser.getUserId(), foundUser.getUserId());
    }

    @Test
    public void getUser_nonExistingUser_throwsException() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(ResponseStatusException.class, () -> userService.getUser(1L));
    }

    @Test
    public void deleteUser_existingUser_success() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        userService.deleteUser(1L);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
    }

    @Test
    public void deleteUser_nonExistingUser_throwsException() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(1L));
    }

    

    @Test
    public void testSetAndGetLobbyId() {
        // Arrange
        User user = new User();
        Long expectedLobbyId = 100L;

        // Act
        user.setLobbyId(expectedLobbyId);
        Long actualLobbyId = user.getLobbyId();

        // Assert
        assertEquals(expectedLobbyId, actualLobbyId, "The getter should return the value that was set by the setter.");
    }

    

    

}