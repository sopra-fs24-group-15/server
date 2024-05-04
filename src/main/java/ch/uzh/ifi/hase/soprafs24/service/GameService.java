package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Template;

import java.util.*;

import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
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

  private GameRepository gameRepository;

  @Autowired
  private TemplateService templateService;

  @Autowired
  public GameService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
    this.gameRepository = gameRepository;
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

  public Round getRound(long lobbyId){
      Round round = gameRepository.findByLobbyId(lobbyId).orElse(null);
      if(round == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Round not found");
      }
      return round;
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
    Lobby lobby = getLobby(lobbyId);
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    setRoundScore(round);
    for (long userId : lobby.getPlayers()){
      updateScore(game, userId, round.getScore(userId));
    
    }
  }


  public void setVote(long lobbyId, long userId){
    Game game = getGame(lobbyId);
    Round round = game.getRound();
    Voting voting = round.getVoting();
    Integer currentVote = voting.getUserVote(userId);
    voting.setUserVote(userId, currentVote + 1);
    round.setSubmittedVotes(round.getSubmittedVotes() + 1);
  }

  public int getSubmittedVotes(long lobbyId){
      Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
      if(lobby == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
      }
      Game game = getGame(lobbyId);
      Round round = game.getRound();
      int currentRound = game.getRound().getCurrentRound();

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
  



  public void setRoundScore(Round round){
    Voting voting = round.getVoting();
    //get the votes in a hashtable
    Map<Long, Integer> votes = voting.getUserVotes();
    //get the votes in a list and sort them
    List<Map.Entry<Long, Integer>> list = new ArrayList<>(votes.entrySet());
    Collections.sort(list, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    //counter to check for the best 3
    int counter = 0;
    for (Map.Entry<Long, Integer> entry : list){
      //if the player has 0 votes he gets no points
      if (entry.getValue() == 0){
        round.addScore(entry.getKey(), 0);
      }
      //the first 3 get points
      else if (counter < 3){
        round.addScore(entry.getKey(), 3-counter);
      }
      //the rest gets 0 points
      else{
        round.addScore(entry.getKey(), 0);
      }
      counter++;
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


  public void endGame(Long lobbyId, Game game){
    Lobby lobby = getLobby(lobbyId);
    lobby.setGameActive(false);
  }
}
