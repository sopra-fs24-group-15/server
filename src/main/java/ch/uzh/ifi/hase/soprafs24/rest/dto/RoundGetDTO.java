package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;
import ch.uzh.ifi.hase.soprafs24.entity.Round;

public class RoundGetDTO {
    
    private Long LobbyId;
    private Long RoundId;
    private Boolean gameInEdit;

    public Long getLobbyId() { 
        return LobbyId; 
    }

    public void setLobbyId(Long LobbyId) { 
        this.LobbyId = LobbyId; 
    }

    public Long getRoundId() { 
        return RoundId; 
    }

    public void setRoundId(Long RoundId) { 
        this.RoundId = RoundId; 
    }

    public Boolean getGameInEdit() {return gameInEdit;}

    public void setGameInEdit(Boolean gameInEdit) {this.gameInEdit = gameInEdit;}
}
