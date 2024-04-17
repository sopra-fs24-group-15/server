package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;


/**
 * User Service
 * This c lass is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final LobbyRepository lobbyRepository;

  @Autowired
  public UserService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
    this.lobbyRepository = lobbyRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }


  public User createUser(String username) {
        // Check if username is already taken
      Optional<User> found = userRepository.findByUsername(username);
      if (found.isPresent()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
      }

        // Create a new user if the username is unique
      User newUser = new User();
      newUser.setUsername(username);
      newUser = userRepository.saveAndFlush(newUser);
      log.debug("Created Information for User: {}", newUser);
      return newUser;
  }


    //TODO update class diagram updateProfile() (chrigi)

  // TODO Implement updateProfilePicture method (chrigi) we could save some limited pictures in the backend
  public void updateProfilePicture(Long userId, String profilePicture) {
      User user = userRepository.findById(userId).orElseThrow(() ->
              new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
      user.setProfilePicture(profilePicture);
      userRepository.save(user);
  }

  public void deleteUser(Long userId) {
      User user = userRepository.findById(userId).orElseThrow(() ->
              new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
      userRepository.delete(user);
  }

}
