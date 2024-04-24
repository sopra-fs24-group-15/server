package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
    private final GameService gameService;
    private final LobbyRepository lobbyRepository;

    GameController(GameService gameService, LobbyRepository lobbyRepository) {
        this.gameService = gameService;
        this.lobbyRepository = lobbyRepository;
    }

    //working postman tested(GS)
    //return a bit more useful infoprmation
    @PutMapping("/lobbys/{lobbyId}/settings/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GamePutDTO createGame(@RequestBody GamePutDTO gamePutDTO,@PathVariable("lobbyId") Long lobbyId, @PathVariable("userId") Long userId) {
        // convert API user to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePutDTOtoEntity(gamePutDTO);
        System.out.println("GamePutDTO: " + gamePutDTO);

        // create game
        Game startGame = gameService.createGame(lobbyId, userId, gameInput.getTotalRounds(), GameMode.BASIC, gameInput.getTimer());
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGamePutDTO(startGame);
    }
  
  
    @PostMapping("/lobbys/{lobbyId}/start/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("userId") Long userId) {
        // start Game
        gameService.startGame(lobbyId, userId);
    }

    /*
    @PostMapping("/round")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startNextRound(@RequestBody GamePostDTO gamePostDTO, @PathVariable("lobbyId") Long lobbyId) {
        // convert API user to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        Long gameId = gameInput.getGameId();
        // start Next Round
        gameService.startNextRound(gameId);
    }*/
}
