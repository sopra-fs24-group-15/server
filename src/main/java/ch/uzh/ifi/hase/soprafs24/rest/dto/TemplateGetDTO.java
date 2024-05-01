package ch.uzh.ifi.hase.soprafs24.rest.dto;

/**
 * Data Transfer Object for exposing only the URL of a Template entity.
 */
public class TemplateGetDTO {

    private String url;  
    private String templateId;
    private int boxCount;

    // Getter method for URL
    public String getUrl() {
        return url;
    }

    // Setter method for URL
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter method for templateId
    public String getTemplateId() {
        return templateId;
    }

    // Setter method for templateId
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    // Getter method for boxCount
    public int getBoxCount() {
        return boxCount;
    }

    // Setter method for boxCount
    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }



}