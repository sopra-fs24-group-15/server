package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

  private Long userId;
  private boolean lobbyOwner;
  private String username;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = this.userId;
  }

  public boolean getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(boolean lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
