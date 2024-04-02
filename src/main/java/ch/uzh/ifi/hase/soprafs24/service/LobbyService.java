package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
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
import java.util.UUID;

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

  public List<Lobby> getLobbys() {
    return this.lobbyRepository.findAll();
  }

  //TODO add to class diagram
  public Lobby getLobby(Long id){
    Lobby foundLobby = this.lobbyRepository.findById(id).orElse(null);
    if(foundLobby==null){
      throw new ResponseStatusException((HttpStatus.NOT_FOUND), "The lobby you searched for doesn't exist");
    }
    return foundLobby;
  }

  //TODO maybe change to return a object lobby
  public Long createLobby(Long userId) {
    Lobby newLobby;
    
    newLobby.setLobbyOwner(userId);

    //Saves the lobby in the repository(needs flushing)
    newLobby = lobbyRepository.save(newLobby);
    userRepository.flush();

    return newLobby.getLobbyId();
  }

  //TODO implement update Lobby logic
  public void updateLobbyOwner(Long userId, Long lobbyId) {
    //finding the lobby by the id 
    Lobby lobbyToChange = getLobby(lobbyId);
    //checking if the user even exists
    this.userRepository.findById(userId);
    //change the owner
    lobbyToChange.setLobbyOwner(userId);
  }

  //TODO implement delete Lobby logic
  public void deleteLobby(Long lobbyId) {
    //finding the lobby by the id 
    Lobby lobbyToDelete = getLobby(lobbyId);
    //deleting the lobby
    lobbyRepository.delete(lobbyToDelete);

  }

  //TODO implement findlobbybyjoincode logic
  public Long findLobbyByJoinCode(Long lobbyJoinCode) {
    return null;
  }

  //TODO check if every List was updated to use long and not User also change the clas diagram
  //TODO if we need total users add this to join lobby logic
  public void joinLobby(Long userId, Long lobbyId) {
    //finding the lobby by the id 
    Lobby lobbyToJoin = getLobby(lobbyId);
    //checking if the user even exists
    this.userRepository.findById(userId);
    //adding the user to the lobby
    lobbyToJoin.setPlayer(userId);
  }

  //TODO implement leavelobby logic
  //TODO if we need total users add this to leave lobby logic
  public void leaveLobby(Long userId, Long lobbyId) {
    //finding the lobby by the id 
    Lobby lobbyToLeave = getLobby(lobbyId);
    //checking if the user even exists
    this.userRepository.findById(userId);
    //removing the user from the lobby
    lobbyToLeave.removePlayer(userId);
  }
}
