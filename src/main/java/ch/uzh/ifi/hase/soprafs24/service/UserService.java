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
import java.util.Random;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;







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
  private final LobbyService lobbyService;


  @Autowired
  public UserService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository, LobbyService lobbyService) {
    this.userRepository = userRepository;
    this.lobbyRepository = lobbyRepository;
    this.lobbyService = lobbyService;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
  }


  public User createUser(String username, boolean lobbyOwner, String profilePicture) {
        // Check if username is already taken
      Optional<User> found = userRepository.findByUsername(username);
      if (found.isPresent()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
      }

        // Create a new user if the username is unique
      User newUser = new User();
      newUser.setUserReady (false);
      newUser.setLobbyOwner(lobbyOwner);
      newUser.setUsername(username);
      newUser.setProfilePicture(profilePicture);
      newUser = userRepository.saveAndFlush(newUser);
      log.debug("Created Information for User: {}", newUser);
      return newUser;
  }


    //TODO update class diagram updateProfile() (chrigi)

  // TODO Implement updateProfilePicture method (chrigi) we could save some limited pictures in the backend
  public void updateProfilePicture(Long userId, String profilePicture) {
      User user = getUser(userId);
      user.setProfilePicture(profilePicture);
      userRepository.save(user);
  }

  public void deleteUser(Long userId) {
    // Find the user by their ID
    User user = getUser(userId);

    // Check if the user is in a lobby
    Long lobbyId = user.getLobbyId();

    if (lobbyId != null) {
        // Retrieve the lobby by its ID
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found."));

        // Remove the user from the lobby's list of players
        lobby.removePlayer(userId);
        if(lobby.getPlayers().size() < 3){
            lobby.setGameActive(false);
        }

        // If the user was the lobby owner, assign the lobby to another player
        if (user.getLobbyOwner()) {
            List<Long> players = lobby.getPlayers();
            if (!players.isEmpty()) {
                // Assign a new owner if players remain
                Long newOwnerId = players.get(0);
                lobby.setLobbyOwner(newOwnerId);

                // Update the new owner's User entity to mark them as the lobby owner
                User newOwner = getUser(newOwnerId);
                if (newOwner != null) {
                    newOwner.setLobbyOwner(true);
                    userRepository.save(newOwner);
                }
              } 
        else {
            // No players left, so delete the lobby
            lobbyRepository.delete(lobby);
            lobbyId = null; // Prevent further processing if the lobby is deleted
          }
        }

        // Persist changes to the lobby if it wasn't deleted
        if (lobbyId != null) {
            lobbyRepository.saveAndFlush(lobby);
        }
    }

    // Delete the user and persist the changes
    userRepository.delete(user);
}

  public void setUserReady(Long userId){
      User user = userRepository.findById(userId).orElseThrow(() ->
              new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
      user.setUserReady(true);
      userRepository.saveAndFlush(user);
  }

}
