package ch.uzh.ifi.hase.soprafs24.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "MEME")
public class Meme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memeId;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data.url")
    @Column(nullable = false, length = 2048)
    private String memeURL;

    @JsonProperty("data.page_url")
    private String pageUrl;

    private String creator; // username of the user who created the meme
    private int votes; // the number of votes the meme received

    @Column()
    private String textTop;

    @Column()
    private String textBottom;

    private Long userId;

    private Long lobbyId;

    // getters and setters
    public Long getMemeId() {
        return memeId;
    }

    public void setMemeId(Long memeId) {
        this.memeId = memeId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMemeURL() {
        return memeURL;
    }

    public void setMemeURL(String memeURL) {
        this.memeURL = memeURL;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getTextTop() {return textTop;}
    public void setTextTop(String textTop) {this.textTop = textTop;}

    public String getTextBottom() {return textBottom;}
    public void setTextBottom(String textBottom) {this.textBottom = textBottom;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public Long getLobbyId() {return lobbyId;}
    public void setLobbyId(Long lobbyId) {this.lobbyId = lobbyId;}

}