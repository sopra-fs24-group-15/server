package ch.uzh.ifi.hase.soprafs24.service;

import java.util.Hashtable;

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
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to
 * the game
 */

//TODO discuss how to handle the game search with lobbyrepo or own repo for game(GS) probably use lobbyrepo and get game from lobby 
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

 
  public Game getGame(long lobbyId){
    Lobby lobby = lobbyRepository.findById(lobbyId);
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
    Lobby lobby = lobbyRepository.findById(lobbyId);
    if(lobby == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found");
    }
    Game game = new Game();
    Hashtable<Long, Integer> tempRanking = new Hashtable<Long, Integer>();
    for (long userId : lobby.getPlayers()){
      tempRanking.put(userId, 0);
    }
    game.setRanking(tempRanking);
    game.setTotalRounds(totalRounds);
    game.setGameMode(gameMode);
    game.setTimer(timer);
    lobby.setGame(game);
    return game;
  }

  public void startGame(long lobbyId, long gameId){
    Game game = getGame(lobbyId);
    game.setCurrentRound(0);
    nextRound(lobbyId);
  }


  public boolean nextRound(long lobbyId){
    Game game = getGame(lobbyId);
    if (game.getCurrentRound() < game.getTotalRounds()){
      game.setCurrentRound(game.getCurrentRound() + 1);
      //TODO implement round logic
      return true;
    }
    else{
      endGame(game);
      return false;
    }
  }


  //TODO discuss if we want to use a list and update with loop or send every request individually(GS)
  public void updateRanking(long lobbyId, long gameId, long userId, int score){
    Game game = getGame(lobbyId);
    Hashtable<Long, Integer> ranking = game.getRanking();
    int tempscore = game.getRank(userId);
    ranking.put(userId, (score + tempscore));
    game.setRanking(ranking);
  }


  public void endGame(Game game){
    //TODO implement end game logic
  }


  public void delteGame(long gameId){
    //TODO implement delete game logic
  }
}
