package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;
import ch.uzh.ifi.hase.soprafs24.entity.User;


//TODO change to match Lobby entitiy
public class LobbyGetDTO {

  private Long lobbyId;
  private List<User> players;
  private long totalUsers;
  private long lobbyJoinCode;
  private long lobbyOwner;
  private boolean gameActive;



  public Long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
    this.lobbyId = lobbyId;
  }

  private long getTotalUsers() {
    return totalUsers;
  }

  private void setTotalUsers(long totalUsers) {
    this.totalUsers = totalUsers;
  }

  private long getLobbyJoinCode() {
    return lobbyJoinCode;
  }

  private void setLobbyJoinCode(long lobbyJoinCode) {
    this.lobbyJoinCode = lobbyJoinCode;
  }

  private long getLobbyOwner() {
    return lobbyOwner;
  }

  private void setLobbyOwner(long lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

  private boolean isGameActive() {
    return gameActive;
  }

  private void setGameActive(boolean gameActive) {
    this.gameActive = gameActive;
  }

}


