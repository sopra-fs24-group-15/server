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

    @GetMapping("/lobby/{lobbyId}/round")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RoundGetDTO getUsersStillEditing(@RequestBody RoundGetDTO roundGetDTO, @PathVariable("lobbyId") Long lobbyId) {
        // convert API user to internal representation
        Round gameInput = DTOMapper.INSTANCE.convertRoundGetDTOtoEntity(roundGetDTO);
        Long gameId = gameInput.getGameId();
        // see set roundInEdit Boolean in round &return round (jana)
        // see set roundInEdit Boolean in round &return round (jana)
        Round roundInEdit = GameService.getUsersStillEditing(gameId);
        return DTOMapper.INSTANCE.convertEntityToRoundGetDTO(roundInEdit);
    }
}