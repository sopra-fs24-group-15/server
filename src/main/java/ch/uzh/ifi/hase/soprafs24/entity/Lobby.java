package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lobbyId;

    @Column(nullable = false, unique = true)
    private String lobbyJoinCode;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "lobby_players", joinColumns = @JoinColumn(name = "lobby_id"))
    @Column(name = "player_id")
    private List<Long> players = new ArrayList<>();

    @Column()
    private Long lobbyOwner;

    @Column()
    private Boolean gameActive;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "gameId")
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

    public void setPlayers(List<Long> players) {
        this.players = players;
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