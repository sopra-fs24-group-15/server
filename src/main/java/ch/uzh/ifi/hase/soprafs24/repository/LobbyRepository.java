package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//TODO change to specification
@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<User, Long> {
  User findByName(String name);

  User findByUsername(String username);
}
