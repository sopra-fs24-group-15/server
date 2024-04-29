package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class MemeGetDTO {
    
    private Long UserId;
    private String MemeURL;

    public Long getUserId() { 
        return UserId; 
    }

    public void setUserId(Long UserId) { 
        this.UserId = UserId; 
    }

    public String getMemeURL() { 
        return MemeURL; 
    }

    public void setMemeURL(String MemeURL) { 
        this.MemeURL = MemeURL; 
    }
}
