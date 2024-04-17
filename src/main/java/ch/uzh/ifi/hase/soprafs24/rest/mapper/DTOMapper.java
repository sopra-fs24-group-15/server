package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */

//TODO change post and get for lobby to match entity and needs
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "lobbyOwner", target = "lobbyOwner")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "username", target = "username")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "players", target = "players")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  //TODO: only quickfix, need to check Mapping again
  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "players", target = "players")
  //@Mapping(source = "lobbyJoinCode", target = "lobbyJoinCode")
  //@Mapping(source = "lobbyOwner", target = "lobbyOwner")
  //@Mapping(source = "gameActive", target = "gameActive")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);
 
  @Mapping(source = "gameId", target = "gameId")
  @Mapping(source = "lobbyId", target = "lobbyId")
  Game convertGamePutDTOtoEntity(GamePutDTO gamePutDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "gameId", target = "gameId")
  GamePutDTO convertEntityToGamePutDTO(Game game);

  //@Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "roundId", target = "roundId")
  RoundGetDTO convertEntityToRoundGetDTO(Round round);
}
