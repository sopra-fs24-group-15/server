package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/lobbys/{lobbyId}")
public class GameController {
    private final GameService gameService;
    private final LobbyRepository lobbyRepository;

    GameController(GameService gameService, LobbyRepository lobbyRepository) {
        this.gameService = gameService;
        this.lobbyRepository = lobbyRepository;
    }

    //working postman tested(GS)
    //return a bit more useful information
    @PostMapping("/settings/{userId}")
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

    @GetMapping("/settings")
    public GameGetDTO getSettings(@PathVariable("lobbyId") Long lobbyId){
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameService.getGame(lobbyId));
    }
    
  
    //working postman tested(GS)
    //works without nextroundstart in startgame
    @PutMapping("/start/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void startGame(@PathVariable("lobbyId") Long lobbyId, @PathVariable("userId") Long userId) {
        // start Game
        gameService.startGame(lobbyId, userId);
    }

    @GetMapping("/rounds")
    public Integer getcurrentRound(@PathVariable("lobbyId") Long lobbyId) {
        Game game = gameService.getGame(lobbyId);
        return game.getRound().getCurrentRound();
    }
    


    //working postman tested(GS)
    @PostMapping("/rounds/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean startNextRound(@PathVariable("lobbyId") Long lobbyId) {
        // start Next Round
        return gameService.startNextRound(lobbyId);
    }

    //working postman tested(GS)
    @PutMapping("/rounds/end")
    @ResponseStatus(HttpStatus.OK)
    public void endRound(@PathVariable("lobbyId") Long lobbyId) {
        // end Round
        gameService.endRound(lobbyId);
    }

    //working postman tested(GS)
    //here userid ios the id of the user who gets the vote not the user who votes therfore in the request body(GS)
    @PostMapping("/votes")
    @ResponseStatus(HttpStatus.OK)
    public void vote(@PathVariable("lobbyId") Long lobbyId, @RequestBody Long userId) {
        // vote
        gameService.setVote(lobbyId, userId);
    }

    // how many votes have been submitted (jana)
    @GetMapping("/votes")
    public Integer getSubmittedVotes(@PathVariable("lobbyId") Long lobbyId) {
        Game game = gameService.getGame(lobbyId);
        int submittedVotes = gameService.getSubmittedVotes(lobbyId);
        return submittedVotes;
    }
    
    //working postman tested(GS)
    //get back usergetdto sorted by score
    @GetMapping("/ranks")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScoreGetDTO> getRanking(@PathVariable("lobbyId") Long lobbyId) {
        Game game = gameService.getGame(lobbyId);
        //returns a sorted list with ranks at position 0 is the first place
        List<User> users = gameService.getRanking(lobbyId);
        List<ScoreGetDTO> scoreGetDTOs = new ArrayList<>();

        for (User user : users) {
            ScoreGetDTO scoreGetDTO = DTOMapper.INSTANCE.convertEntityToScoreGetDTO(user);
            scoreGetDTO.setScore(game.getScore(user.getUserId()));
            scoreGetDTOs.add(scoreGetDTO);
        }

        return scoreGetDTOs;
    }

    @GetMapping("/scores")
    public List<ScoreGetDTO> getScores(@PathVariable("lobbyId") Long lobbyId) {
        Game game = gameService.getGame(lobbyId);
        List<User> users = gameService.getPlayers(lobbyId);
        List<ScoreGetDTO> scoreGetDTOs = new ArrayList<>();

        for (User user : users) {
            ScoreGetDTO scoreGetDTO = DTOMapper.INSTANCE.convertEntityToScoreGetDTO(user);
            scoreGetDTO.setScore(game.getScore(user.getUserId()));
            scoreGetDTOs.add(scoreGetDTO);
        }
        return scoreGetDTOs;
    }
    

    @GetMapping("/users/edits")
    @ResponseStatus(HttpStatus.OK)
    public boolean getUsersEditing(@PathVariable("lobbyId") Long lobbyId) {
        return gameService.getUsersStillEditing(lobbyId);
    }
}
