package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;
import ch.uzh.ifi.hase.soprafs24.entity.User;

//TODO change to match lobby entity
public class LobbyPostDTO {

  private long lobbyId;
  private List<User> players;
  private long totalUsers;
  private long lobbyJoinCode;
  private long lobbyOwner;
  private boolean gameActive;

  public long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(long LobbyId) {
    this.lobbyId = lobbyId;
  }

  public List<User> getPlayers() {
    return players;
  }

  public void setPlayers(List<User> players) {
    this.players = players;
  }

  public long getTotalUsers() {
    return totalUsers;
  }

  public void setTotalUsers(long totalUsers) {
    this.totalUsers = totalUsers;
  }

  public long getLobbyJoinCode() {
    return lobbyJoinCode;
  }

  public void setLobbyJoinCode(long lobbyJoinCode) {
    this.lobbyJoinCode = lobbyJoinCode;
  }

  public long getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(long lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

  public boolean isGameActive() {
    return gameActive;
  }

  public void setGameActive(boolean gameActive) {
    this.gameActive = gameActive;
  }

}
