package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will  be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */

//TODO change according to class diagram
@Entity
@Table(name = "USER")
public class User implements Serializable {


  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username;

  //TODO define how and from where to get the profile picture. (chrigi)
  @Column(unique = true)
  private String profilePicture;

  @Column()
  private Long lobbyId;

  @Column(nullable = false)
  private Boolean lobbyOwner;

  @Column(nullable = false)
  private Boolean userReady;


  //getter, setter and update functions
  public Long getUserId() {
      return userId;
  }

  public void setUserId(Long userId) {
      this.userId = userId;
  }

  public String getUsername() {
      return username;
  }

  public void setUsername(String username) {
      this.username = username;
  }

  //TODO How to get this and display it? (chrigi)
  public String getProfilePicture() {
      return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
      this.profilePicture = profilePicture;
  }

  public Long getLobbyId() {
      return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
      this.lobbyId = lobbyId;
  }

  public Boolean getLobbyOwner() {
      return lobbyOwner;
  }

  public void setLobbyOwner(Boolean lobbyOwner) {
      this.lobbyOwner = lobbyOwner;
  }

  public Boolean getUserReady() {
        return userReady;
  }

  public void setUserReady(Boolean userReady) {
        this.userReady = userReady;
  }

}
