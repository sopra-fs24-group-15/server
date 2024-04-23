package ch.uzh.ifi.hase.soprafs24.rest.dto;



//TODO change to match Lobby entity
public class LobbyPutDTO {

    private String lobbyJoinCode;

    public String getLobbyJoinCode() {
        return lobbyJoinCode;
    }

    public void setLobbyJoinCode(String lobbyJoinCode) {
        this.lobbyJoinCode = lobbyJoinCode;
    }
}