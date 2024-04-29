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

    @JsonProperty("data.url")
    @Column(nullable = false, length = 2048)
    private String memeURL;

    @Column(nullable = false)
    private Long userId;


    // getters and setters
    public Long getMemeId() {
        return memeId;
    }

    public void setMemeId(Long memeId) {
        this.memeId = memeId;
    }

    public String getMemeURL() {
        return memeURL;
    }

    public void setMemeURL(String memeURL) {
        this.memeURL = memeURL;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}