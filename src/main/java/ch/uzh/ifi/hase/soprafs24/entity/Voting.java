package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.Hashtable;

import javax.persistence.*;

@Entity
@Table(name = "VOTING")
public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ElementCollection
    @CollectionTable(name = "user_votes", joinColumns = {@JoinColumn(name = "vote_id")})
    @Column(name = "votes")
    private Hashtable<Long, Integer> userVotes = new Hashtable<>();

    @ElementCollection
    @CollectionTable(name = "memes_votes", joinColumns = {@JoinColumn(name = "vote_id")})
    @Column(name = "meme_id")
    private Hashtable<Long, Long> memesDict = new Hashtable<>();


    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Hashtable<Long, Integer> getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(Hashtable<Long, Integer> userVotes) {
        this.userVotes = userVotes;
    }

    public Hashtable<Long, Long> getMemesDict() {
        return memesDict;
    }

    public void setMemesDict(Hashtable<Long, Long> memesDict) {
        this.memesDict = memesDict;
    }



    public void displayMemes(Hashtable<Long, Long> memesDict) {
        //TODO Implementation to display memes chrigi
        //put it in a service
    }

    public void voteMeme(Long memeId, Long userId, Long voteId) {
        //TODO Implementation for a user to vote on a meme chrigi
        // service
    }

    public int getVotes(Long userId, Long voteId) {
        //TODO Implementation to get the number of votes from a user chrigi
        // service
        return userVotes.getOrDefault(userId, 0);
    }
}