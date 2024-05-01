package ch.uzh.ifi.hase.soprafs24.rest.dto;

/**
 * Data Transfer Object for exposing only the URL of a Template entity.
 */
public class TemplateGetDTO {

    private String url;  // Only URL field to be included in the DTO

    //private String theme; //TODO remove for the themed gamemode

    /*// Getter method for theme
    public String getTheme() {
        return theme;
    }

    // Setter method for theme
    public void setTheme(String theme) {
        this.theme = theme;
    }*///TODO remove for themed mode

    // Getter method for URL
    public String getUrl() {
        return url;
    }

    // Setter method for URL
    public void setUrl(String url) {
        this.url = url;
    }
}