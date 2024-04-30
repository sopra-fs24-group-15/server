package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Voting;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

public class GameServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetGame_gameFound_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        Game game = gameService.getGame(1L);
        assertNotNull(game);
        assertEquals(mockGame, game);
    }

    @Test
    public void testGetGame_lobbyNotFound_throwsException() {
        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));
    }

    @Test
    public void testCreateGame_success() {
        Lobby mockLobby = new Lobby();
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockLobby.setLobbyOwner(1L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Game game = gameService.createGame(1L, 1L, 5, GameMode.BASIC, 60);
        assertNotNull(game);
        assertEquals(1L, mockLobby.getLobbyOwner());
        assertEquals(5, game.getTotalRounds());
        assertEquals(GameMode.BASIC, game.getGameMode());
        assertEquals(60, game.getTimer());
    }

    @Test
    public void testStartGame_notLobbyOwner_throwsException() {
        Lobby mockLobby = new Lobby();
        User mockUser = new User();
        mockUser.setUserId(2L);
        mockLobby.setLobbyOwner(1L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        assertThrows(ResponseStatusException.class, () -> gameService.startGame(1L, 2L));
    }

    @Test
    public void testStartGame_notEnoughPlayers_throwsException() {
        Lobby mockLobby = new Lobby();
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockLobby.setLobbyOwner(1L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        assertThrows(ResponseStatusException.class, () -> gameService.startGame(1L, 1L));
    }

    //TODO chrigi throws error because of template service(GS)
    /*@Test
    public void testNextRound_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockGame.setTotalRounds(3);
        mockGame.setCurrentRound(1);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        assertTrue(gameService.startNextRound(1L));
        assertEquals(2, mockGame.getCurrentRound());
    }*/
    

    @Test
    public void testNextRound_notEnoughPlayers_throwsException() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockGame.setTotalRounds(3);
        mockGame.setCurrentRound(1);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        assertThrows(ResponseStatusException.class, () -> gameService.startNextRound(1L));
    }

    @Test
    public void testNextRound_returnFalseAfterRoundLimit_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockGame.setTotalRounds(3);
        mockGame.setCurrentRound(3);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        assertFalse(gameService.startNextRound(1L));
    }

    @Test
    public void testSetRoundScore_standardVotes_success() {
        Voting voting = new Voting();
        HashMap<Long, Integer> votes = new HashMap<>();
        votes.put(1L, 10);
        votes.put(2L, 20);
        votes.put(3L, 30);
        votes.put(4L, 40);
        voting.setUserVotes(votes);

        Round round = new Round();
        round.setVoting(voting);

        gameService.setRoundScore(round);

        assertEquals(3, round.getScore(1L));
        assertEquals(2, round.getScore(2L));
        assertEquals(1, round.getScore(3L));
        assertEquals(0, round.getScore(4L));
    }

    @Test
    public void testSetRoundScore_nullVotes_success() {
        Voting voting = new Voting();
        HashMap<Long, Integer> votes = new HashMap<>();
        votes.put(1L, 0);
        votes.put(2L, 0);
        votes.put(3L, 0);
        voting.setUserVotes(votes);

        Round round = new Round();
        round.setVoting(voting);

        gameService.setRoundScore(round);

        assertEquals(0, round.getScore(1L));
        assertEquals(0, round.getScore(2L));
        assertEquals(0, round.getScore(3L));
    }

    //TODO implement tie logic first
    @Test
    public void testSetRoundScore_tieVotes_success() {
        Voting voting = new Voting();
        HashMap<Long, Integer> votes = new HashMap<>();
        votes.put(1L, 30);
        votes.put(2L, 30);
        votes.put(3L, 30);
        voting.setUserVotes(votes);

        Round round = new Round();
        round.setVoting(voting);

        gameService.setRoundScore(round);

        assertEquals(3, round.getScore(1L));
        assertEquals(2, round.getScore(2L));
        assertEquals(1, round.getScore(3L)); 
    } 

    @Test
    public void testGetRanking_regularCase_success() {
        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);
        User user3 = new User();
        user3.setUserId(3L);
        User user4 = new User();
        user4.setUserId(4L);
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockLobby.setGame(mockGame);
        HashMap<Long, Integer> scores = new HashMap<>();
        scores.put(1L, 10);
        scores.put(2L, 30);
        scores.put(3L, 20);
        scores.put(4L, 40);
        mockGame.setScores(scores);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user3));
        when(userRepository.findById(4L)).thenReturn(Optional.of(user4));

        List<User> ranking = gameService.getRanking(1L);
        assertEquals(Arrays.asList(user4, user2, user3, user1), ranking);
    }

    @Test
    public void testGetRanking_allZeroScores_success() {
        User user1 = new User();
        user1.setUserId(1L);
        User user2 = new User();
        user2.setUserId(2L);
        User user3 = new User();
        user3.setUserId(3L);
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockLobby.setGame(mockGame);
        HashMap<Long, Integer> scores = new HashMap<>();
        scores.put(1L, 0);
        scores.put(2L, 0);
        scores.put(3L, 0);
        mockGame.setScores(scores);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user3));

        List<User> ranking = gameService.getRanking(1L);
        // The exact order doesn't matter here since all scores are the same
        assertEquals(3, ranking.size());
        assertTrue(ranking.containsAll(Arrays.asList(user3, user2, user1)));
    }

    //TODO implement tie logic first(GS)
    /*@Test
    public void testGetRanking_tieScores_success() {
        Game mockGame = new Game();
        HashMap<Long, Integer> scores = new HashMap<>();
        scores.put(1L, 20);
        scores.put(2L, 20);
        scores.put(3L, 40);
        mockGame.setScores(scores);

        when(gameService.getGame(anyLong())).thenReturn(mockGame);

        List<Long> ranking = gameService.getRanking(1L);
        assertTrue(ranking.indexOf(3L) < ranking.indexOf(1L) && ranking.indexOf(1L) < ranking.indexOf(2L) ||
                   ranking.indexOf(3L) < ranking.indexOf(2L) && ranking.indexOf(2L) < ranking.indexOf(1L));
    } */

    @Test
    public void testSetVote_sucess() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        Voting mockVoting = new Voting();
        mockRound.setVoting(mockVoting);
        mockGame.setCurrentRound(1);
        mockGame.setRound(mockRound);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));
          for (long userId : mockLobby.getPlayers()){
            mockVoting.setUserVote(userId, 0);
          }

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        gameService.setVote(1L, 1L);
        gameService.setVote(1L, 1L);
        gameService.setVote(1L, 1L);
        gameService.setVote(1L, 2L);
        gameService.setVote(1L, 2L);
        assertEquals(3, mockVoting.getUserVotes().get(1L));
        assertEquals(2, mockVoting.getUserVotes().get(2L));
    }

    @Test
    public void testEndGame_gameEnds_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        

        gameService.endGame(1L, mockGame);
        assertFalse(mockLobby.getGameActive());
    }
}
