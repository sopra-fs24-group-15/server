package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserGetDTO {


  private String username;
  private Long userId;
  private Long profilePicture;
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

  public Long getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(Long profilePicture) {
    this.profilePicture = profilePicture;
  }

  public boolean getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(boolean lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

}