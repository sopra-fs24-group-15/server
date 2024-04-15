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

    @Column(unique = true)
    private String lobbyJoinCode;

    @ElementCollection
    private List<Long> players = new ArrayList<>();

    @Column()
    private Long lobbyOwner;

    @Column()
    private Boolean gameActive;

    @Column()
    private Game game;

    // Autowire LobbyRepository
    @Autowired
    private LobbyRepository lobbyRepository;

    public Lobby() {
        generateUniqueJoinCode(); // Generate join code upon lobby creation
    }

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

    // Method to generate a unique join code
    private void generateUniqueJoinCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        boolean uniqueCodeGenerated = false;

        // Keep generating until a unique code is found
        while (!uniqueCodeGenerated) {
            StringBuilder joinCode = new StringBuilder();
            while (joinCode.length() < 6) { // 6 characters long
                int index = (int) (rnd.nextFloat() * characters.length());
                joinCode.append(characters.charAt(index));
            }
            String potentialCode = joinCode.toString();
            // Check if the potential code already exists in the database
            if (!checkIfJoinCodeExists(potentialCode)) {
                this.lobbyJoinCode = potentialCode;
                uniqueCodeGenerated = true;
            }
        }
    }

    // Helper method to check if the join code already exists in the database
    private boolean checkIfJoinCodeExists(String code) {
        Optional<Lobby> existingLobby = lobbyRepository.findByJoinCode(code);
        return existingLobby.isPresent();
    }
}