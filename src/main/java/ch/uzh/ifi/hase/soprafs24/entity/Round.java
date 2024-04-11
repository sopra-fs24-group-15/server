import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

@Entity
@Table(name = "Round")
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    //TODO activate template
    //@Column()
    //private Template template;

    @ElementCollection
    private Hashtable<Long, Integer> roundScore = new Hashtable<>();

    @Column()
    private int currentRound;


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

    //TODO activate template
    /* 
    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
    */

    public Hashtable<Long, Integer> getRanking() {
        return roundScore;
    }

    public void setRanking(Hashtable<Long, Integer> roundScore) {
        this.roundScore = roundScore;
    }

    public void addScore(Long userId, Integer score) {
        this.roundScore.put(userId, score);
    }
}