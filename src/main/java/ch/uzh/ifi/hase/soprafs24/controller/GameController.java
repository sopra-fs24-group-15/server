package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
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


    @PutMapping("/lobby/{lobbyId}/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GamePutDTO createGame(@RequestBody GamePutDTO gamePutDTO,@PathVariable("lobbyId") Long lobbyId, int totalRounds, GameMode gameMode, int timer) {
        // convert API user to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePutDTOtoEntity(gamePutDTO);

        // create game
        Game startGame = gameService.createGame(lobbyId, lobbyId, totalRounds, gameMode, timer);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToGamePutDTO(startGame);
    }

    @PutMapping("/lobby/{lobbyId}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void startGame(@RequestBody GamePutDTO gamePutDTO,@PathVariable("lobbyId") Long lobbyId, Long userId) {
        // convert API user to internal representation
        Game gameInput = DTOMapper.INSTANCE.convertGamePutDTOtoEntity(gamePutDTO);
        // start Game
        gameService.startGame(lobbyId, userId);
    }
}
