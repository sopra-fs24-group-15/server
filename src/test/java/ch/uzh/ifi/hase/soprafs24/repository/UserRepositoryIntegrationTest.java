package ch.uzh.ifi.hase.soprafs24.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs24.entity.User;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setUsername("testperson");
        user.setLobbyOwner(false);
        user.setUserReady(false);

        // Persisting user to the in-memory database
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername(user.getUsername());

        // then
        assertTrue(found.isPresent()); // Ensure the found user is present
        assertEquals(found.get().getUsername(), user.getUsername());
    }

    @Test
    public void findbyUsername_failure() {
        // given
        User user = new User();
        user.setUsername("testperson");
        user.setLobbyOwner(false);
        user.setUserReady(false);

        // Persisting user to the in-memory database
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername("wrongname"); // Use a different username
        
        // then
        assertTrue(found.isEmpty()); // Ensure the found user is not present
    }
}

