package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPostDTO {

  private Long userId;
  private boolean lobbyOwner;
  private String username;
  private String profilePicture;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = this.userId;
  }

  public String getUsername() {
    return username;
  }

  public boolean getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(boolean lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }
}