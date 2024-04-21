package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class TemplateService {

    private final RestTemplate restTemplate;
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(RestTemplate restTemplate, TemplateRepository templateRepository) {
        this.restTemplate = restTemplate;
        this.templateRepository = templateRepository;
    }


    
    public List<Template> fetchTemplate() {
        String url = "https://api.imgflip.com/get_memes";
        try {
            String result = restTemplate.getForObject(url, String.class);
    
            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);
            JsonNode memes = root.path("data").path("memes");
    
            // Convert each meme in the response to a Template object and save it in the database
            // As I understand it we fetch the memes from the API and save them in the database so we can use them later (chrigi)
            List<Template> templates = new ArrayList<>();
            for (JsonNode meme : memes) {
                Template template = new Template();
                template.setTemplateId(meme.path("id").asText());
                template.setName(meme.path("name").asText());
                template.setUrl(meme.path("url").asText());
                template.setWidth(meme.path("width").asInt());
                template.setHeight(meme.path("height").asInt());
                template.setBoxCount(meme.path("box_count").asInt());
                templates.add(template);
    
                templateRepository.save(template);
            }
            return templates;
        } catch (HttpClientErrorException | IOException e) {
            //TODO handle exception
            return null;
        }

    }

    //return template for user
    // I think we have templates in the database and we want to fix one template for each round (chrigi)
    public Template getTemplateForUser() {
        //TODO implement
        return templateRepository.getOne(3L);
    }
}
