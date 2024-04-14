package ch.uzh.ifi.hase.soprafs24.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
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

  
  public Game createGame(long lobbyId, int totalRounds, GameMode gameMode, int timer){
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    Game game = new Game();
    Hashtable<Long, Integer> tempScores = new Hashtable<Long, Integer>();
    for (long userId : lobby.getPlayers()){
      tempScores.put(userId, 0);
    }
    game.setScores(tempScores);
    game.setTotalRounds(totalRounds);
    game.setGameMode(gameMode);
    game.setTimer(timer);
    lobby.setGame(game);
    return game;
  }

  public void startGame(long lobbyId){
    Game game = getGame(lobbyId);
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    lobby.setGameActive(true);
    game.setCurrentRound(0);
    nextRound(lobbyId);
  }


  public boolean nextRound(long lobbyId){
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    Game game = getGame(lobbyId);
    if (game.getCurrentRound() < game.getTotalRounds()){
      game.setCurrentRound(game.getCurrentRound() + 1);
      Round round = new Round();
      round.setCurrentRound(game.getCurrentRound());
      game.setRound(round);
      //TODO implement game play with template and voting 
      //setRoundScore(round);
      for (long userId : lobby.getPlayers()){
        updateScore(game, userId, round.getScore(userId));
      }
      return true;
    }
    else{
      endGame(lobbyId, game);
      return false;
    }
  }

  //TODO discuss how to handle when votes are the same(GS)
  //TODO create voting and then activate this method maybe changing to getting the votes are needed(GS)
  /* 
  public void setRoundScore(Round round){
    //get the votes in a hashtable
    Hashtable<Long, Integer> votes = new Hashtable<Long, Integer>();
    for (long userId : voting.getVotes().keySet()){
      votes.put(userId, voting.getVotes().get(userId).size());
    }
    //get the votes in a list and sort them
    List<Map.Entry<Long, Integer>> list = new ArrayList<>(votes.entrySet());
    Collections.sort(list, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    //counter to check for the best 3
    int counter = 0;
    for (Map.Entry<Long, Integer> entry : list){
      //if the player has 3 votes he gets no points
      if (entry.getValue() == null){
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
  }*/


  //could also be iplemented directly in entity
  public void updateScore(Game game, long userId, int score){
    int tempscore = game.getScore(userId);
    game.setScore(userId, tempscore + score);
  }


  //gives back a sorted list with the ranking of the players first at position 0 and so on
  public List<Long> getRanking(long lobbyId){
    Game game = getGame(lobbyId);
    List<Map.Entry<Long, Integer>> list = new ArrayList<>(game.getScores().entrySet());
    Collections.sort(list, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    List<Long> ranking = new ArrayList<Long>();
    for (Map.Entry<Long, Integer> entry : list){
      ranking.add(entry.getKey());
    }
    return ranking;
  }


  public void endGame(Long lobbyId, Game game){
    Lobby lobby = lobbyRepository.findById(lobbyId).orElse(null);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    lobby.setGameActive(false);
    //TODO implement end game logic(GS)
  }

  //TODO probably not needed(GS)
  public void delteGame(long gameId){
    //TODO implement delete game logic
  }
}
