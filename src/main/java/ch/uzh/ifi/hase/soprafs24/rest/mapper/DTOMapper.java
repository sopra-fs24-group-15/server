package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.*;
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

@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "lobbyOwner", target = "lobbyOwner")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "userId", target = "userId")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "userId", target = "userId")
  UserDeleteDTO convertEntityToUserDeleteDTO(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "userId", target = "userId")
  User convertUserDeleteDTOtoEntity(UserDeleteDTO userDeleteDTO);

  @Mapping(source = "lobbyOwner", target = "lobbyOwner")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  @Mapping(source = "lobbyJoinCode", target = "lobbyJoinCode")
  Lobby convertLobbyPutDTOtoEntity(LobbyPutDTO lobbyPutDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  Lobby convertLobbyDeleteDTOtoEntity(LobbyDeleteDTO lobbyDeleteDTO);

  @Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "players", target = "players")
  @Mapping(source = "lobbyJoinCode", target = "lobbyJoinCode")
  @Mapping(source = "lobbyOwner", target = "lobbyOwner")
  @Mapping(source = "gameActive", target = "gameActive")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

  @Mapping(source = "totalRounds", target = "totalRounds")
  @Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "timer", target = "timer")
  GameGetDTO convertEntityToGameGetDTO(Game game);
 
  @Mapping(source = "totalRounds", target = "totalRounds")
  //@Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "timer", target = "timer")
  Game convertGamePutDTOtoEntity(GamePutDTO gamePutDTO);

  @Mapping(source = "totalRounds", target = "totalRounds")
  //@Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "timer", target = "timer")
  GamePutDTO convertEntityToGamePutDTO(Game game);

  //@Mapping(source = "lobbyId", target = "lobbyId")
  @Mapping(source = "roundId", target = "roundId")
  //@Mapping(source= "gameInEdit", target = "gameInEdit")
  RoundGetDTO convertEntityToRoundGetDTO(Round round);

  @Mapping(source = "roundId", target = "roundId")
  //@Mapping(source= "gameInEdit", target = "gameInEdit")
  Round convertRoundGetDTOtoEntity(RoundGetDTO roundGetDTO);

  //@Mapping(source = "roundId", target = "roundId")
  //@Mapping(source = "gameId", target = "gameId")
  //@Mapping(source = "lobbyId", target = "lobbyId")
  //Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

  //@Mapping(source = "roundId", target = "roundId")
  //GamePostDTO convertEntitytoGamePostDTO(Game game);

  //@Mapping(source = "theme", target = "theme") //TODO remove for themed mode
  @Mapping(source = "url", target = "url")
  @Mapping(source = "templateId", target = "templateId")
  @Mapping(source = "boxCount", target = "boxCount")
  TemplateGetDTO convertEntityToTemplateGetDTO(Template template);

  @Mapping(source = "memeURL", target = "memeURL")
  @Mapping(source = "userId", target = "userId")
  MemeGetDTO convertEntityToMemeGetDTO(Meme meme);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "userId", target = "userId")
  ScoreGetDTO convertEntityToScoreGetDTO(User user); 
}
