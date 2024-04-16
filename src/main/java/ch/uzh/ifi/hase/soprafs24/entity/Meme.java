package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "MEME")
public class Meme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memeId;

    @Column(nullable = false, length = 2048)
    private String memeURL;

    @ElementCollection
    private List<String> text;

    @Column(nullable = false)
    private int votes;


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

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    // Methods as per class diagram

    public long createMeme(Long templateId, List<String> text) {

        //TODO how to implemet it. where to store things how to communicate with fromntend (chrigi)
        // Implementation to create a meme

        return 0L;
    }

    public String getMeme(Long memeId) {
        //TODO (chrigi)
        // Implementation to retrieve a meme's URL by its ID

        return this.memeURL;
    }
}