package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RoundGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



//TODO implement Rest interface calls
@RestController
public class RoundController {
  private final GameService GameService;

  RoundController(GameService gameService) {
    this.GameService = gameService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoundGetDTO getUsersStillEditing(@RequestBody Long gameId) {
    //find the game
    Game foundGame = GameService.getGame(gameId);
    //get the round
    Round foundRound = GameService.getCurrentRound(foundGame);
    //get the number of users still editing
    Long UsersStillEditing = GameService.getUsersStillEditing(foundRound);
    //convert internal representation of round back to API
    return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(foundRound);
  }



  
}