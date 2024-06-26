package ch.uzh.ifi.hase.soprafs24.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

/*    @Test
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

    @Test
    public void findByJoinCode_failure() {
        // given
        Lobby lobby = new Lobby();
        lobby.setLobbyJoinCode("123456");
        lobby.setLobbyOwner(1L);

        // Persisting lobby to the in-memory database
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Optional<Lobby> found = lobbyRepository.findByLobbyJoinCode("654321"); // Use a different join code
        
        // then
        assertTrue(found.isEmpty()); // Ensure the found lobby is not present
    }*/
}