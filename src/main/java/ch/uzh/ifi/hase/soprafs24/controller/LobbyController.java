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

  @PostMapping("/lobbys")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO LobbyPostDTO) {
    //create a lobby object from the input
    Lobby LobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(LobbyPostDTO);
    //create a lobby by calling user service
    Lobby createdLobby = lobbyService.createLobby(LobbyInput.getLobbyOwner());
    //convert internal representation of lobby back to API
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
  }

  @GetMapping("/lobbys")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<LobbyGetDTO> getAllLobbys() {
    //fetch all lobbys in the internal representation
    List<Lobby> lobbys = lobbyService.getLobbys();
    List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

    //convert each lobby to the API representation
    for (Lobby lobby : lobbys) {
      lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    return lobbyGetDTOs;
  }

  @GetMapping("/lobbys/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public LobbyGetDTO retrieveLobbyToJoin(@RequestBody String lobbyJoinCode, Long userId) {
    //retrieves a lobby to join
    Lobby foundLobby = lobbyService.findLobbyByJoinCode(lobbyJoinCode);
    //join the lobby
    lobbyService.joinLobby(userId, foundLobby);
    // convert internal representation of lobby back to API
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(foundLobby);
  }

  
  @DeleteMapping("/lobbys/{lobbyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void deleteLobby(@RequestBody Long lobbyId, Long userId) {
    //deletes the lobby
    lobbyService.deleteLobby(lobbyId, userId);
  }

}
