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

    //TODO first finish implementing nextRound
    /* 
    @Test
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
    }
    */

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
    /*@Test
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

        assertEquals(2, round.getScore(1L));
        assertEquals(1, round.getScore(2L));
        assertEquals(0, round.getScore(3L)); // Assumes specific tie-breaking logic
    } */

    @Test
    public void testGetRanking_regularCase_success() {
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

        List<Long> ranking = gameService.getRanking(1L);
        assertEquals(Arrays.asList(4L, 2L, 3L, 1L), ranking);
    }

    @Test
    public void testGetRanking_allZeroScores_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        mockLobby.setGame(mockGame);
        HashMap<Long, Integer> scores = new HashMap<>();
        scores.put(1L, 0);
        scores.put(2L, 0);
        scores.put(3L, 0);
        mockGame.setScores(scores);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        List<Long> ranking = gameService.getRanking(1L);
        // The exact order doesn't matter here since all scores are the same
        assertEquals(3, ranking.size());
        assertTrue(ranking.containsAll(Arrays.asList(1L, 2L, 3L)));
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
    public void testEndGame_gameEnds_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        

        gameService.endGame(1L, mockGame);
        assertFalse(mockLobby.getGameActive());
    }
}
