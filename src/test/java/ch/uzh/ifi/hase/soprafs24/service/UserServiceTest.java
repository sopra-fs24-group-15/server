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

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");

        // when -> any object is being saved in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being saved in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.existsByUsername("testUsername")).thenReturn(false);
        User createdUser = userService.createUser("testUsername");

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        Mockito.when(userRepository.existsByUsername("testUsername")).thenReturn(true);

        // then -> attempt to create second user with same username -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser("testUsername"));
    }
}
