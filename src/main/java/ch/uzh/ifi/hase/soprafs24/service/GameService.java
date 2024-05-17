package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Template;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Voting;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to
 * the game
 */

@Service
@Transactional
public class GameService {

  private final Logger log = LoggerFactory.getLogger(LobbyService.class);

  private final LobbyRepository lobbyRepository;

  private final UserRepository userRepository;

  @Autowired
  private TemplateService templateService;

  @Autowired
  public GameService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
  }

 //helper function to get game from lobbyId
  public Game getGame(long lobbyId){
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    Game game = new Game();
    game = lobby.getGame();
    if(game == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
    }
    return game;
  }

  //helper function to get lobby from lobbyId
  public Lobby getLobby(long lobbyId){
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    return lobby;
  }

  //helper function to get user from userId
  public User getUser(long userId){
    User user = userRepository.findById(userId).orElse(null);
    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    return user;
  }

  public void updateIfUsersLeave(long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    Voting voting = round.getVoting();

    //check if player has left and if remove from scoring and voting
    List<Long> players = lobby.getPlayers();

    if(voting != null){
    //update voting
    Map<Long, Integer> votes = voting.getUserVotes();
    List<Long> removevotes = new ArrayList<Long>();

    for(Long userId: votes.keySet()){
      if(players.contains(userId)){
        continue;
      }
      else{
        removevotes.add(userId);
      }
    }

    for(Long userId: removevotes){
      votes.remove(userId);
    }
    }
    //update RoundScore
    if (round != null){
    Map<Long, Integer> roundscores = round.getRoundScore();
    List<Long> removeroundscores = new ArrayList<Long>();
    for(Long userId: roundscores.keySet()){
      if(players.contains(userId)){
        continue;
      }
      else{
        removeroundscores.add(userId);
      }
    }
    for(Long userId: removeroundscores){
      roundscores.remove(userId);
    }
    round.setRoundScore(roundscores);
    //update meme
    List<Meme> memes = round.getMemes();
    List<Meme> removememes = new ArrayList<Meme>();
    for(Meme meme: memes){
      if(players.contains(meme.getUserId())){
        continue;
      }
      else{
        removememes.add(meme);
      }
    }
    for(Meme meme: removememes){
      memes.remove(meme);
    }
    round.setMemes(memes);
    }
    //update game score
    if(game != null){
    Map<Long, Integer> scores = game.getScores();
    List<Long> removescores = new ArrayList<Long>();
    for(Long userId: scores.keySet()){
      if(players.contains(userId)){
        continue;
      }
      else{
        removescores.add(userId);
      }
    }
    for(Long userId: removescores){
      scores.remove(userId);
    }
    game.setScores(scores);
    }
  }
  
  public Game createGame(long lobbyId, long userId, int totalRounds, GameMode gameMode, int timer){
    Lobby lobby = getLobby(lobbyId);
    User user = getUser(userId);
    if(!Objects.equals(lobby.getLobbyOwner(), user.getUserId())){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the lobby owner can start the game");
    }

    Game game = new Game();
    HashMap<Long, Integer> tempScores = new HashMap<Long, Integer>();
    for (Long id : lobby.getPlayers()){
      tempScores.put(id, 0);
    }
    game.setScores(tempScores);
    game.setTotalRounds(totalRounds);
    game.setGameMode(gameMode);
    game.setTimer(timer);
    lobby.setGame(game);
    lobbyRepository.flush();
    return game;
  }

  public void startGame(long lobbyId, long userId){
    Game game = getGame(lobbyId);
    Lobby lobby = getLobby(lobbyId);
    User user = getUser(userId);
    if(!Objects.equals(lobby.getLobbyOwner(), user.getUserId())){
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the lobby owner can start the game");
    }
    if(lobby.getPlayers().size() < 3){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "At least 3 players are required to start the game");
    }
    lobby.setGameActive(true);
    game.setCurrentRound(0);
  }

  //TODO implement correct
  public boolean getUsersStillEditing(Long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    Game game = getGame(lobbyId);
    Round round = game.getRound();

    //implement
    return true;
  }

  public boolean startNextRound(long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    Game game = getGame(lobbyId);
    if (game.getCurrentRound() < game.getTotalRounds()){
      if(lobby.getPlayers().size() < 3){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "At least 3 players are required to play");
      }
      game.setCurrentRound(game.getCurrentRound() + 1);
      Round round = new Round();
      round.setCurrentRound(game.getCurrentRound());
      for (long userId : lobby.getPlayers()){
        round.addScore(userId, 0);
      }
      game.setRound(round);
      Voting voting = new Voting();
      for (long userId : lobby.getPlayers()){
        voting.setUserVote(userId, 0);
      }
      round.setVoting(voting);
      //    updateIfUsersLeave(lobbyId);
      //You get a random Template of 100 (chrigi)
      Template template = templateService.fetchTemplate();
      round.setTemplate(template);
      round.setSubmittedVotes(0);
      return true;
    }
    else{
      endGame(lobbyId, game);
      return false;
    }
  }

  public void endRound(long lobbyId){
    updateIfUsersLeave(lobbyId);
    Lobby lobby = getLobby(lobbyId);
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    Voting voting = round.getVoting();
    setRoundScore(round, lobby);
    for (long userId : lobby.getPlayers()){
      updateScore(game, userId, round.getScore(userId));
      User user = getUser(userId);
      if(user.getBestScore() == null || user.getBestScore() <= voting.getUserVote(userId)){
        user.setBestScore(voting.getUserVote(userId));
        List<Meme> roundMemes = round.getMemes();
        for (Meme meme : roundMemes){
          if(meme.getUserId() == userId){
            user.setBestMeme(meme.getMemeURL());
          }
        }
      }
    }
  }


  public void setVote(long lobbyId, long userId){
    updateIfUsersLeave(lobbyId);
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    Voting voting = round.getVoting();
    Integer currentVote = voting.getUserVote(userId);
    voting.setUserVote(userId, currentVote + 1);
    round.setSubmittedVotes(round.getSubmittedVotes() + 1);
  }

  public int getSubmittedVotes(long lobbyId){
    updateIfUsersLeave(lobbyId);
      Game game = getGame(lobbyId);
      Round round = game.getRound();
      return round.getSubmittedVotes();
  }

  public void setMeme(long lobbyId, long userId, String memeURL){
    Round round = getGame(lobbyId).getRound();
    Meme meme = new Meme();
    meme.setMemeURL(memeURL);
    meme.setUserId(userId);
    round.addMeme(meme);
  }

  public List<Meme> getMemes(long lobbyId, long userId){
    Round round = getGame(lobbyId).getRound();
    List<Meme> allMemes = new ArrayList<>(round.getMemes()); // create a copy of the list
    allMemes.removeIf(meme -> meme.getUserId() == userId);
    return allMemes;
  }
  

  public void setRoundScore(Round round, Lobby lobby){
    updateIfUsersLeave(lobby.getLobbyId());
    Voting voting = round.getVoting();
    // Get the votes in a hashtable
    Map<Long, Integer> votes = voting.getUserVotes();
    
    // Get the votes in a list and sort them
    List<Map.Entry<Long, Integer>> list = new ArrayList<>(votes.entrySet());
    list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

    int currentScore = 3;  // Start score for the top scorer
    int usersAwarded = 0;  // Counter for how many users have been awarded a score

    for (int i = 0; i < list.size(); i++) {
        Map.Entry<Long, Integer> entry = list.get(i);
        // 0 votes => 0 points
      if (entry.getValue() == 0){
        round.addScore(entry.getKey(), 0);
        } else {
            // Award the current score if this is the first entry or if it matches the previous entry's votes
            if (i == 0 || list.get(i).getValue().equals(list.get(i - 1).getValue())) {
                if (usersAwarded < 3) {
                    round.addScore(entry.getKey(), currentScore);
                    usersAwarded++;
                } else {
                    round.addScore(entry.getKey(), 0);  // No points beyond the top 3 scores
                }
            } else {
                // If current entry's votes do not match the previous one, decrement the score
                if (usersAwarded < 3) {
                    currentScore = 3 - usersAwarded;
                    round.addScore(entry.getKey(), currentScore);
                    usersAwarded++;
                } else {
                    round.addScore(entry.getKey(), 0);  // Outside of top 3 => 0 points
          }
        }
      }
    }
  }

  //could also be implemented directly in entity
  public void updateScore(Game game, long userId, int score){
    int tempscore = game.getScore(userId);
    game.setScore(userId, tempscore + score);
  }

  public List<User> getPlayers(long lobbyId){
    Lobby lobby = getLobby(lobbyId);
    List<User> players = new ArrayList<User>();
    for (long userId : lobby.getPlayers()){
      User tempUser = getUser(userId);
      players.add(tempUser);
    }
    return players;
  }

  //gives back a sorted list with the ranking of the players first at position 0 and so on
  public List<User> getRanking(long lobbyId){
    Game game = getGame(lobbyId);
    List<Map.Entry<Long, Integer>> list = new ArrayList<>(game.getScores().entrySet());
    Collections.sort(list, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    List<User> ranking = new ArrayList<User>();
    for (Map.Entry<Long, Integer> entry : list){
      User tempUser = getUser(entry.getKey());
      ranking.add(tempUser);
    }
    return ranking;
  }

  //TODO add setbestscore to testing
  public void endGame(Long lobbyId, Game game){
    updateIfUsersLeave(lobbyId);
    for (long userId : getLobby(lobbyId).getPlayers()){
      User user = getUser(userId);
      user.setBestScore(0);
    }
    Lobby lobby = getLobby(lobbyId);
    lobby.setGameActive(false);
  }

  public void setTopic(long lobbyId, String topic) {
    // Retrieve the game and associated round
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    Template template = round.getTemplate();

    template.setTopic(topic);
}

public boolean isUserWithLowestScore(long lobbyId, long userId) {
  Game game = getGame(lobbyId);
  Round round = game.getRound();

  // Check if the lowest scorer is already determined for this round
  Long existingLowestScorer = round.getLowestScorerUserId();
  if (existingLowestScorer != null) {
      return existingLowestScorer.equals(userId);
  }

  // Fetch all player scores and find the lowest score value
  Map<Long, Integer> scores = game.getScores();
  int minScore = Collections.min(scores.values());

  // Collect all user IDs with the lowest score
  List<Long> lowestScoringUsers = new ArrayList<>();
  for (Map.Entry<Long, Integer> entry : scores.entrySet()) {
      if (entry.getValue() == minScore) {
          lowestScoringUsers.add(entry.getKey());
      }
  }

  if (lowestScoringUsers.isEmpty()) {
      return false;
  }

   // Select a random user ID from those with the lowest score
   Random random = new Random();
   Long randomlySelectedUserId = lowestScoringUsers.get(random.nextInt(lowestScoringUsers.size()));
   round.setLowestScorerUserId(randomlySelectedUserId);

   // Return true if the randomly selected user ID matches the given user ID
   return randomlySelectedUserId.equals(userId);
  }
}
