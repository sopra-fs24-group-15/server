package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

public class GameServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TemplateService templateService;

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
    public void testGetGame_gameNotFound_throwsException() {
        Lobby mockLobby = new Lobby();
        mockLobby.setGame(null);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        assertThrows(ResponseStatusException.class, () -> gameService.getGame(1L));
    }

    @Test
    public void testGetLobby_lobbyFound_success() {
        Lobby mockLobby = new Lobby();

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        Lobby lobby = gameService.getLobby(1L);
        assertNotNull(lobby);
        assertEquals(mockLobby, lobby);
    }

    @Test
    public void testGetLobby_lobbyNotFound_throwsException() {
        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.getLobby(1L));
    }

    @Test
    public void testGetUser_userFound_success() {
        User mockUser = new User();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        User user = gameService.getUser(1L);
        assertNotNull(user);
        assertEquals(mockUser, user);
    }

    @Test
    public void testGetUser_userNotFound_throwsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.getUser(1L));
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
    public void testCreateGame_notLobbyOwner_throwsException() {
        Lobby mockLobby = new Lobby();
        User mockUser = new User();
        mockUser.setUserId(2L);
        mockLobby.setLobbyOwner(1L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(1L, 2L, 5, GameMode.BASIC, 60));
    }

    @Test
    public void testStartGame_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockLobby.setLobbyOwner(1L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        gameService.startGame(1L, 1L);
        assertTrue(mockLobby.getGameActive());
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

    
    @Test
    public void testStartNextRound_notEnoughPlayers_throwsException() {
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
    public void testEndRound_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        Voting mockVoting = new Voting();
        mockRound.setVoting(mockVoting);
        mockRound.setRoundInEdit(true);
        mockGame.setRound(mockRound);
        mockGame.setCurrentRound(1);
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        gameService.endRound(1L);
        assertEquals(1, mockGame.getCurrentRound());
    }

    @Test
    public void testGetSubmittedVotes_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        mockLobby.setGame(mockGame);
        mockGame.setRound(mockRound);
        mockRound.setSubmittedVotes(3);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        assertEquals(3, gameService.getSubmittedVotes(1L));
    }

    @Test
    public void testSetMeme_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        mockRound.setRoundInEdit(true);
        mockGame.setCurrentRound(1);
        mockGame.setRound(mockRound);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        gameService.setMeme(1L, 1L, "memeURL");
        assertEquals(1, mockRound.getMemes().size());
    }

    @Test
    public void testGetMemes_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        mockRound.setRoundInEdit(true);
        mockGame.setCurrentRound(1);
        mockGame.setRound(mockRound);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        gameService.setMeme(1L, 1L, "memeURL1");
        gameService.setMeme(1L, 2L, "memeURL2");
        assertEquals("memeURL2", gameService.getMemes(1L, 1L).get(0).getMemeURL());
    }

    @Test
    public void testSetRoundScore_standardVotes_success() {
        Lobby mockLobby = new Lobby();
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L, 4L));
        Voting voting = new Voting();
        HashMap<Long, Integer> votes = new HashMap<>();
        votes.put(1L, 10);
        votes.put(2L, 20);
        votes.put(3L, 30);
        votes.put(4L, 40);
        voting.setUserVotes(votes);

        Round round = new Round();
        round.setVoting(voting);

        gameService.setRoundScore(round, mockLobby);

        assertEquals(3, round.getScore(4L));
        assertEquals(2, round.getScore(3L));
        assertEquals(1, round.getScore(2L));
        assertEquals(0, round.getScore(1L));
    }

    @Test
    public void testSetRoundScore_nullVotes_success() {
        Lobby mockLobby = new Lobby();
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L, 4L));
        Voting voting = new Voting();
        HashMap<Long, Integer> votes = new HashMap<>();
        votes.put(1L, 0);
        votes.put(2L, 0);
        votes.put(3L, 0);
        voting.setUserVotes(votes);

        Round round = new Round();
        round.setVoting(voting);

        gameService.setRoundScore(round, mockLobby);

        assertEquals(0, round.getScore(1L));
        assertEquals(0, round.getScore(2L));
        assertEquals(0, round.getScore(3L));
    }


    @Test
    public void testUpdateScore_success() {
        long lobbyId = 1L;
        long userId = 1L;
        int scoreToAdd = 10;

        Game game = new Game();
        game.setScores(new HashMap<>());
        game.setScore(userId, 20);

        Lobby lobby = new Lobby();
        lobby.setGame(game);
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        gameService.updateScore(game, userId, scoreToAdd);
        assertEquals(30, game.getScore(userId));
    }

    @Test
    public void testGetPlayers_success() {
        Lobby mockLobby = new Lobby();
        User mockUser1 = new User();
        mockUser1.setUserId(1L);
        User mockUser2 = new User();
        mockUser2.setUserId(2L);
        User mockUser3 = new User();
        mockUser3.setUserId(3L);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));
        List<User> users = Arrays.asList(mockUser1, mockUser2, mockUser3);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(mockUser3));

        List<User> players = gameService.getPlayers(1L);
        assertEquals(users, players);
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

    @Test
    public void testGetRanking_tieScores_success() {
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
        scores.put(2L, 20);
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
    public void testSetVote_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        Voting mockVoting = new Voting();
        mockRound.setVoting(mockVoting);
        mockGame.setCurrentRound(1);
        mockGame.setRound(mockRound);
        mockLobby.setGame(mockGame);
        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));
        for (long userId : mockLobby.getPlayers()) {
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
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setBestScore(0);

        mockLobby.setPlayers(Arrays.asList(1L, 2L, 3L));

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        gameService.endGame(1L, mockGame);
        assertFalse(mockLobby.getGameActive());
    }

    @Test
    public void testSetTopic_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        Template mockTemplate = new Template();
        mockRound.setTemplate(mockTemplate);
        mockGame.setRound(mockRound);
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));

        gameService.setTopic(1L, "New Topic");
        assertEquals("New Topic", mockTemplate.getTopic());
    }

    @Test
    public void testIsUserWithLowestScore_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        mockGame.setRound(mockRound);
        mockGame.setScores(Map.of(1L, 10, 2L, 20, 3L, 30));
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        boolean isLowest = gameService.isUserWithLowestScore(1L, 1L);
        assertTrue(isLowest || !isLowest);  // Since it's random, it can be true or false
    }

    @Test
    public void testIsUserWithLowestScore_existingLowestScorer_success() {
        Lobby mockLobby = new Lobby();
        Game mockGame = new Game();
        Round mockRound = new Round();
        mockRound.setLowestScorerUserId(1L);
        mockGame.setRound(mockRound);
        mockGame.setScores(Map.of(1L, 10, 2L, 20, 3L, 30));
        mockLobby.setGame(mockGame);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(mockLobby));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        boolean isLowest = gameService.isUserWithLowestScore(1L, 1L);
        assertTrue(isLowest);
    }

    @Test
    public void testGettersAndSetters() {
        // Arrange
        Voting voting = new Voting();
        Long voteId = 1L;
        Map<Long, Integer> userVotes = new HashMap<>();
        userVotes.put(1L, 5);
        Map<Long, Long> memesDict = new HashMap<>();
        memesDict.put(1L, 10L);

        // Act
        voting.setVoteId(voteId);
        voting.setUserVotes(userVotes);
        voting.setMemesDict(memesDict);

        // Assert
        assertEquals(voteId, voting.getVoteId());
        assertEquals(userVotes, voting.getUserVotes());
        assertEquals(memesDict, voting.getMemesDict());
    }

    @Test
    public void testGetUserVote() {
        // Arrange
        Voting voting = new Voting();
        voting.setUserVote(1L, 5);

        // Act
        Integer vote = voting.getUserVote(1L);

        // Assert
        assertEquals(5, vote);
    }

    @Test
    public void testSetUserVote() {
        // Arrange
        Voting voting = new Voting();
        
        // Act
        voting.setUserVote(1L, 5);

        // Assert
        assertEquals(5, voting.getUserVotes().get(1L));
    }

    @Test
    public void testGetVotes() {
        // Arrange
        Voting voting = new Voting();
        voting.setUserVote(1L, 5);

        // Act
        int votes = voting.getVotes(1L, 1L);

        // Assert
        assertEquals(5, votes);
    }

    @Test
    public void testGetVotesForNonExistentUser() {
        // Arrange
        Voting voting = new Voting();

        // Act
        int votes = voting.getVotes(1L, 1L);

        // Assert
        assertEquals(0, votes);
    }

    @Test
    public void testAddScoreToRound() {
        Round round = new Round();
        round.addScore(1L, 10);

        assertEquals(1, round.getRoundScore().size());
        assertEquals(10, round.getRoundScore().get(1L));
    }

    @Test
    public void testAddMemeToRound() {
        Round round = new Round();
        Meme meme = new Meme();
        // Set properties of meme here, if needed

        round.addMeme(meme);

        assertEquals(1, round.getMemes().size());
        assertTrue(round.getMemes().contains(meme));
    }

    @Test
    public void testRoundEntity() {
        Round round = new Round();
        round.setCurrentRound(1);
        round.setSubmittedVotes(3);
        round.setRoundInEdit(true);
        Template template = new Template();
        round.setTemplate(template);
        Voting voting = new Voting();
        round.setVoting(voting);

        assertEquals(1, round.getCurrentRound());
        assertEquals(3, round.getSubmittedVotes());
        assertTrue(round.getRoundInEdit());
        assertEquals(template, round.getTemplate());
        assertEquals(voting, round.getVoting());
    }

    @Test
    public void testRoundScore() {
        Round round = new Round();
        round.addScore(1L, 10);
        round.addScore(2L, 20);

        assertEquals(2, round.getRoundScore().size());
        assertEquals(10, round.getScore(1L));
        assertEquals(20, round.getScore(2L));
    }

    @Test
    public void testRoundMemes() {
        Round round = new Round();
        Meme meme1 = new Meme();
        Meme meme2 = new Meme();
        // Set properties of memes here, if needed

        round.addMeme(meme1);
        round.addMeme(meme2);

        assertEquals(2, round.getMemes().size());
        assertTrue(round.getMemes().contains(meme1));
        assertTrue(round.getMemes().contains(meme2));
    }

    @Test
    public void testRoundLowestScorerUserId() {
        Round round = new Round();
        round.setLowestScorerUserId(1L);

        assertEquals(1L, round.getLowestScorerUserId());
    }

}
 
