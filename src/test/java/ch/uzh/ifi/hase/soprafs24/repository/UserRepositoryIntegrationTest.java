/*package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    /*  //TODO dont work like this (chrigi)
    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setUsername("testUsername");
    
        // Check if there are other properties that need to be set
        // For example, if your User entity has a non-nullable 'email' field
        // user.setEmail("testEmail@example.com");
    
        // Save the user to the database
        userRepository.save(user);
    
        entityManager.flush();
    
        // when
        Optional<User> found = userRepository.findByUsername(user.getUsername());
    
        // then
        assertTrue(found.isPresent()); 
        assertEquals(found.get().getUsername(), user.getUsername());
    }
    */
        
      
    }
