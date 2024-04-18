package ch.uzh.ifi.hase.soprafs24.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void findByJoinCode_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setLobbyJoinCode("123456"); // Use a unique join code for testing
        lobby.setLobbyOwner(1L); // Assuming lobbyOwner is a user ID

        // Persisting lobby to the in-memory database
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Optional<Lobby> found = lobbyRepository.findByLobbyJoinCode(lobby.getLobbyJoinCode());

        // then
        assertTrue(found.isPresent()); // Ensure the found lobby is present
        assertEquals(found.get().getLobbyJoinCode(), lobby.getLobbyJoinCode());
        assertEquals(found.get().getLobbyOwner(), lobby.getLobbyOwner());
    }
}