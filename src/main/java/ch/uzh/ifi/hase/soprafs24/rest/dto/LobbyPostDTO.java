package ch.uzh.ifi.hase.soprafs24.rest.dto;


//TODO change to match lobby entity
public class LobbyPostDTO {


  private long lobbyId;
  private List<Long> players;
  private String lobbyJoinCode;
  private boolean gameActive;

  private Long lobbyOwner;

  public Long getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(Long lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }


  public long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(long LobbyId) {
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

  public boolean isGameActive() {
    return gameActive;
  }

  public void setGameActive(boolean gameActive) {
    this.gameActive = gameActive;
  }

}
