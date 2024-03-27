package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
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



  @GetMapping("/lobbys")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<LobbyGetDTO> getAllLobbys() {
    // fetch all users in the internal representation
    List<Lobby> lobbys = lobbyService.getLobbys();
    List<LobbyGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (Lobby lobby : lobbys) {
      lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }
    return lobbyGetDTOs;
  }

  @PostMapping("/lobbys")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
    // convert API user to internal representation
    Lobby lobbyInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

    // create user
    Lobby createdLobby = lobbyService.createLobby(lobbyInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
  }
}
