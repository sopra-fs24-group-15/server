package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ScoreGetDTO {


  private String username;
  private Long userId;
  private int score;
  private Long profilePicture;


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

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Long getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(Long profilePicture) {
    this.profilePicture = profilePicture;
  }
}