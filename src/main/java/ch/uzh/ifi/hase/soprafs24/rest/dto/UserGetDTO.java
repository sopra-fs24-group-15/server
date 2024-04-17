package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserGetDTO {


  private String username;
  private Long userId;
  private boolean lobbyOwner;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public boolean getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(boolean lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }
}
