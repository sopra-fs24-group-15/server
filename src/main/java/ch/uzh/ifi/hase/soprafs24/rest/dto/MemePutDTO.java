package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class MemePutDTO {
    private Long lobbyId;
    private Long userId;
    private String textTop;
    private String textBottom;


    public Long getLobbyId() {return lobbyId;}

    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getTextTop() {return textTop;}
    public void setTextTop(String upperText) {this.textTop = textTop;}

    public String getTextBottom() {return textBottom;}
    public void setTextBottom(String textBottom) {this.textBottom = textBottom;}
}
