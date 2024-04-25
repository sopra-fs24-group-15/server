package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;


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

  public String getLobbyJoinCode() {
    return lobbyJoinCode;
  }

  public void setLobbyJoinCode(String lobbyJoinCode) {
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

