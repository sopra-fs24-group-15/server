package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
@Table(name = "Round")
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private Template template;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "voting_id", referencedColumnName = "voteId")
    private Voting voting;

    @Column()
    private int submittedVotes;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "round_scores", joinColumns = @JoinColumn(name = "round_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "score")
    private Map<Long, Integer> roundScore = new HashMap<>();

    @ManyToMany(cascade = CascadeType.ALL) 
    @JoinTable(
        name = "round_memes",
        joinColumns = @JoinColumn(name = "round_id"),
        inverseJoinColumns = @JoinColumn(name = "meme_id")
    )
    private List<Meme> memes = new ArrayList<>();

    @Column()
    private int currentRound;


    @Column()
    private Boolean roundInEdit;

    @Column
    private Long lowestScorerUserId;


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

    public Map<Long, Integer> getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(Map<Long, Integer> roundScore) {
        this.roundScore = roundScore;
    }

    public void addScore(Long userId, Integer score) {
        this.roundScore.put(userId, score);
    }

    public Integer getScore(Long userId) {
        return this.roundScore.get(userId);
    }

    public List<Meme> getMemes() {
        return memes;
    }

    public void setMemes(List<Meme> memes) {
        this.memes = memes;
    }

    public void addMeme(Meme meme) {
        this.memes.add(meme);
    }
    
    public Boolean getRoundInEdit() {return roundInEdit; }

    public void setRoundInEdit(Boolean roundInEdit) {this.roundInEdit = roundInEdit;}

    public int getSubmittedVotes() {
        return submittedVotes;
    }

    public void setSubmittedVotes(int submittedVotes) {
        this.submittedVotes = submittedVotes;
    }

    public Long getLowestScorerUserId() {
        return lowestScorerUserId;
    }

    public void setLowestScorerUserId(Long lowestScorerUserId) {
        this.lowestScorerUserId = lowestScorerUserId;
    }
}