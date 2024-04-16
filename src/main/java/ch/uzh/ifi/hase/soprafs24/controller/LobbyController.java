package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */

//TODO implement Rest interface calls 
@RestController
public class LobbyController {

  private final LobbyService lobbyService;

  LobbyController(LobbyService lobbyService) {
    this.lobbyService = lobbyService;
  }

  @PostMapping("/lobby")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO LobbyPostDTO) {
    //creates a lobby with the user as the owner
    Lobby LobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(LobbyPostDTO);
    Lobby createdLobby = lobbyService.createLobby(LobbyInput.getLobbyId());
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
  }

  @GetMapping("/lobby/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public LobbyGetDTO retrieveLobbyToJoin(@RequestBody String lobbyId, Long UserId) {
    //retrieves a lobby to join
    Lobby foundLobby = lobbyService.findLobbyByJoinCode(lobbyId);
    // convert internal representation of lobby back to API
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);

  }

  @DeleteMapping("/lobby/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteLobby(@RequestBody Long lobbyId) {
    //deletes a lobby
    lobbyService.deleteLobby(lobbyId);
  }
}
