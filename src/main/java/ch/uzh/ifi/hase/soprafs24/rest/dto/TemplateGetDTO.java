package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.List;

/**
 * Data Transfer Object for exposing only the URL of a Template entity.
 */
public class TemplateGetDTO {

    private String url;  
    private String templateId;
    private int boxCount;
    private List<String> topics;
    private String topic;

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

    // Getter method for topics
    public List<String> getTopics() {
        return topics;
    }

    // Setter method for topics
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    // Getter method for topic
    public String getTopic() {
        return topic;
    }

    // Setter method for topic
    public void setTopic(String topic) {
        this.topic = topic;
    }


}

