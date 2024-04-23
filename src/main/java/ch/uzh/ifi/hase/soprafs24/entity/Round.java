package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Round")
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @Column()
    private Template template;

    @Column()
    private Voting voting;

    @Column
    private HashMap<Long, Integer> roundScore = new HashMap<>();

    @Column()
    private int currentRound;

    @Column()
    private Long gameId;

    @Column()
    private Boolean roundInEdit;


    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
     
    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
    
    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    public HashMap<Long, Integer> getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(HashMap<Long, Integer> roundScore) {
        this.roundScore = roundScore;
    }

    public void addScore(Long userId, Integer score) {
        this.roundScore.put(userId, score);
    }

    public Integer getScore(Long userId) {
        return this.roundScore.get(userId);
    }

    public Long getGameId() {return gameId;}

    public void setGameId(Long gameId) {this.gameId = gameId;}

    public Boolean getRoundInEdit() {return roundInEdit; }

    public void setRoundInEdit(Boolean roundInEdit) {this.roundInEdit = roundInEdit;}
}