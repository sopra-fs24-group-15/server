package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lobbyId;

    @Column(nullable = false, unique = true)
    private String lobbyJoinCode;

    @ElementCollection
    private List<Long> players = new ArrayList<>();

    @Column()
    private Long lobbyOwner;

    @Column()
    private Boolean gameActive;


    @Column()
    private Game game;

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public void addPlayer(Long userId) {
        this.players.add(userId);
    }

    public void removePlayer(Long userId) {
        this.players.remove(userId);
    }

    public String getLobbyJoinCode() {
        return lobbyJoinCode;
    }

    public void setLobbyJoinCode(String lobbyJoinCode) {
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


    public Game getGame() {
        return game;
    }
  
    public void setGame(Game game) {
      this.game = game;
    }
}