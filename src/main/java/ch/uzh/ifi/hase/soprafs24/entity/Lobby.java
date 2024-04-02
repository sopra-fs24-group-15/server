package ch.uzh.ifi.hase.soprafs24.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Internal Lobby Representation
 * This class composes the internal representation of the lobby and defines how
 * the lobby is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */

 //TODO create a Join code
@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long lobbyId;

  @Column()
  private List<Long> players;

  @Column()
  private Long totalUsers;

  @Column()
  private Long lobbyJoinCode;

  @Column()
  private Long lobbyOwner;

  @Column()
  private Boolean gameActive;


  public Long getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(Long lobbyId) {
    this.lobbyId = lobbyId;
  }

  public List<Long> getPlayers() {
    return players;
  }

  public void setPlayer(Long userId) {
    this.players.add(userId);
  }

  public void setPlayers(List<Long> players) {
    this.players = players;
  }

  public void removePlayer(Long userId) {
    this.players.remove(userId);
  }

  //TODO do we even need totalusers can't we just use the len of list players?
  public Long getTotalUsers() {
    return totalUsers;
  }

  public void setTotalUsers(Long totalUsers) {
    this.totalUsers = totalUsers;
  }

  public Long getLobbyJoinCode() {
    return lobbyJoinCode;
  }

  public void setLobbyJoinCode(Long lobbyJoinCode) {
    this.lobbyJoinCode = lobbyJoinCode;
  }

  public Long getLobbyOwner() {
    return lobbyOwner;
  }

  public void setLobbyOwner(Long lobbyOwner) {
    this.lobbyOwner = lobbyOwner;
  }

  public Boolean getGameActive() {
    return gameActive;
  }

  public void setGameActive(Boolean gameActive) {
    this.gameActive = gameActive;
  }

  public Lobby orElse(Object object) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'orElse'");
  }
}
