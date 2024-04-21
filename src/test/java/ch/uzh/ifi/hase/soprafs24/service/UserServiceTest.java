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

        // given
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setLobbyOwner(false);

        // when -> any object is being saved in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(testUser);
    }

    //TODO: test ahpasse: createUser nimmt als input de username als string und lobbyowner als boolean
    
    @Test
    public void createUser_validInputs_success() {
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        User createdUser = userService.createUser("testUsername", false);

        // Then
        assertNotNull(createdUser.getUserId());
        assertEquals(createdUser.getUsername(), testUser.getUsername());
        assertEquals(createdUser.getLobbyOwner(), testUser.getLobbyOwner());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        // given -> a first user has already been created
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        // then -> attempt to create second user with same username -> check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser("testUsername", false));
    }
    
}