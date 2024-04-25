package ch.uzh.ifi.hase.soprafs24.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.*;

@Entity
@Table(name = "VOTING")
public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_votes", joinColumns = @JoinColumn(name = "round_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "votes")
    private Map<Long, Integer> userVotes = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "meme_dict", joinColumns = @JoinColumn(name = "round_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "meme_id")
    private Map<Long, Long> memesDict = new HashMap<>();


    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Map<Long, Integer> getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(Map<Long, Integer> userVotes) {
        this.userVotes = userVotes;
    }

    public Integer getUserVote(long userId) {
        return userVotes.get(userId);
    }

    public void setUserVote(long userId, Integer vote){
        this.userVotes.put(userId, vote);
    }

    public Map<Long, Long> getMemesDict() {
        return memesDict;
    }

    public void setMemesDict(Map<Long, Long> memesDict) {
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