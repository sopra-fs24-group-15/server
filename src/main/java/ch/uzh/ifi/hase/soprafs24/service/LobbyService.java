package ch.uzh.ifi.hase.soprafs24.service;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;


/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class LobbyService {

  private final Logger log = LoggerFactory.getLogger(LobbyService.class);

  private final LobbyRepository lobbyRepository;

  private final UserRepository userRepository;

  @Autowired
  public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
  }

  // Method to generate a unique join code
  private void generateUniqueJoinCode(Lobby lobby) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random rnd = new Random();
    boolean uniqueCodeGenerated = false;

    // Keep generating until a unique code is found
    while (!uniqueCodeGenerated) {
        StringBuilder joinCode = new StringBuilder();
        while (joinCode.length() < 6) { // 6 characters long
            int index = (int) (rnd.nextFloat() * characters.length());
            joinCode.append(characters.charAt(index));
        }
        String potentialCode = joinCode.toString();
        // Check if the potential code already exists in the database
        if (!checkIfJoinCodeExists(potentialCode)) {
            lobby.setLobbyJoinCode(potentialCode);
            uniqueCodeGenerated = true;
        }
    }
  }

// Helper method to check if the join code already exists in the database
  private boolean checkIfJoinCodeExists(String code) {
    Lobby foundLobby = this.lobbyRepository.findByLobbyJoinCode(code).orElse(null);
    if(foundLobby==null){
      return false;
    }
    return true;
  }

  public List<Lobby> getLobbys() {
    return this.lobbyRepository.findAll();
  }

  
  public Lobby getLobby(Long id){
    Lobby foundLobby = this.lobbyRepository.findById(id).orElse(null);
    if(foundLobby==null){
      throw new ResponseStatusException((HttpStatus.NOT_FOUND), id + " not found");
    }
    return foundLobby;
  }

  public Lobby findLobbyByJoinCode(String lobbyJoinCode) {
    Lobby foundLobby = this.lobbyRepository.findByLobbyJoinCode(lobbyJoinCode).orElse(null);
    if(foundLobby==null){
      throw new ResponseStatusException((HttpStatus.NOT_FOUND), "The lobby you searched for doesn't exist");
    }
    return foundLobby;
  }

  
  public Lobby createLobby(Long userId) {
    // Initialize a new lobby
    Lobby newLobby = new Lobby();
    
    // Generate a unique join code for the lobby
    generateUniqueJoinCode(newLobby);
    
    // Add the user to the new lobby and set them as the owner
    newLobby.addPlayer(userId);
    newLobby.setLobbyOwner(userId);
    newLobby.setGameActive(false);

    // Update the user's information to reflect the lobby ownership
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        user.setLobbyId(null); // The lobby's new ID will be assigned after saving
        user.setLobbyOwner(true);
    }

    // Save the lobby in the repository and flush changes to persist
    newLobby = lobbyRepository.save(newLobby);
    lobbyRepository.flush();

    // Update the user's lobby ID to the newly created lobby's ID
    if (user != null) {
        user.setLobbyId(newLobby.getLobbyId());
        userRepository.saveAndFlush(user);
    }

    return newLobby;
}

  public void updateLobbyOwner(Long userId, Long lobbyId) {
    //finding the lobby by the id 
    Lobby lobbyToChange = getLobby(lobbyId);
    //checking if the user even exists
    this.userRepository.findById(userId);
    //change the owner
    lobbyToChange.setLobbyOwner(userId);
  }
  
  public void deleteLobby(Long lobbyId, Long userId) {
    //finding the lobby by the id 
    Lobby lobbyToDelete = getLobby(lobbyId);
    //checking if the user even exists
    User user = userRepository.findById(userId).orElse(null);
    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    //check if user is the owner of the lobby
    if (lobbyToDelete.getLobbyOwner() != userId) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of the lobby");
    }
    //deleting the lobby
    lobbyRepository.delete(lobbyToDelete);

  }

  public void joinLobby(Long userId, Lobby lobbyToJoin) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    if (lobbyToJoin.getGameActive()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot join. The game is already active.");
    }

    if (lobbyToJoin.getPlayers().size() < 8) {
        lobbyToJoin.addPlayer(userId);
        user.setLobbyId(lobbyToJoin.getLobbyId());
    } 
    
    else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The lobby is full");
    }
  }

  public boolean checkIfPlayersAreReady(Lobby lobby) {
    //check if all players are ready
    for (Long player : lobby.getPlayers()) {
      User user = userRepository.findById(player).orElse(null);
      if(user == null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
      if (user.getUserReady() == false){
        return false;
      }
    }
    return true;
  }
}
