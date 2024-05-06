package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column()
    private int totalRounds;

    @Column()
    private int currentRound;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "game_scores", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "player_id")
    @Column(name = "score")
    private Map<Long, Integer> scores = new HashMap<>();

    @Column()
    private GameMode gameMode;

    @Column()
    private int timer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id", referencedColumnName = "roundId")
    private Round round;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public Map<Long, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<Long, Integer> scores) {
        this.scores = scores;
    }

    public void setScore(long userId, int score) {
        scores.put(userId, score);
    }

    public int getScore(long userId) {
        return scores.get(userId);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    //public Long getLobbyId() {return lobbyId;}

    //public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    //public Long getRoundId() {return roundId;}

    //public void setRoundId(Long roundId) {this.roundId = roundId;}
}