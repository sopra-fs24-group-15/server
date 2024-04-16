package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;
import ch.uzh.ifi.hase.soprafs24.entity.User;


//TODO change to match Lobby entity
public class LobbyGetDTO {

  private Long lobbyId;
  private List<Long> players;
  private String lobbyJoinCode;
  private long lobbyOwner;
  private boolean gameActive;


  public Long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
    this.lobbyId = lobbyId;
  }

  public List<Long> getPlayers() {
      return players;
  }

  public void setPlayers(List<Long> players) {
      this.players = players;
  }

    private String getLobbyJoinCode() {
    return lobbyJoinCode;
  }

  private void setLobbyJoinCode(String lobbyJoinCode) {
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


