package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserDeleteDTO {
    private Long userId;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}