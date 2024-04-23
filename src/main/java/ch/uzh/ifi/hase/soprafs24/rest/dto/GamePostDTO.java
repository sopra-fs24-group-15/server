package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GamePostDTO {
    private Long lobbyId;

    private Long userId;

    private Long gameId;

    public Long getLobbyId() {return lobbyId;}

    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {this.userId = userId;}

    public Long getGameId() {return gameId;}

    public void setGameId(Long gameId) {this.gameId = gameId;}
}
