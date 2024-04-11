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

    @ElementCollection
    private Hashtable<Long, Integer> ranking = new Hashtable<>();

    @Column()
    private GameMode gameMode;

    @Column()
    private int timer;

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

    public Hashtable<Long, Integer> getRanking() {
        return ranking;
    }   

    public void setRanking(Hashtable<Long, Integer> ranking) {
        this.ranking = ranking;
    }

    public void setRank(long userId, int score) {
        ranking.put(userId, score);
    }

    public int getRank(long userId) {
        return ranking.get(userId);
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
}