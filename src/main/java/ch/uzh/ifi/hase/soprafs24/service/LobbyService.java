package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
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

  @Autowired
  public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
    this.lobbyRepository = lobbyRepository;
  }

  public List<Lobby> getLobbys() {
    return this.lobbyRepository.findAll();
  }

  //TODO implement create Lobby logic
  public Long createLobby(Long userId) {
    
  }

  //TODO implement update Lobby logic
  public void updateLobbyOwner(Long userId, Long lobbyId) {

  }

  //TODO implement delete Lobby logic
  public void deleteLobby(Long lobbyId) {

  }

  //TODO implement findlobbybyjoincode logic
  public Long findLobbyByJoinCode(Long lobbyJoinCode) {

  }

  //TODO implement joinlobby logic
  public void joinLobby(Long userId, Long lobbyId) {

  }

  //TODO implement leavelobby logic
  public void leaveLobby(Long userId, Long lobbyId) {

  }
}
