package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
  Optional<Lobby> findById(Long id);

  //TODO check if it works after implementing join code in lobby entity
  Optional<Lobby> findByJoinCode(Long joinCode);
}
