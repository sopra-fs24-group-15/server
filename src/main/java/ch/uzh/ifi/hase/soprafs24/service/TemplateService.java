package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;



@Service
public class TemplateService {

    private final RestTemplate restTemplate;
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(RestTemplate restTemplate, TemplateRepository templateRepository) {
        this.restTemplate = restTemplate;
        this.templateRepository = templateRepository;
    }

    public Template fetchTemplate() {
        List<Template> templates = templateRepository.findAll();
        if (templates.isEmpty()) {
            templates = fetchTemplatesFromAPI();
        }
        return selectRandomTemplate(templates);
    }

    private List<Template> fetchTemplatesFromAPI() {
        final String url = "https://api.imgflip.com/get_memes";
        List<Template> newTemplates = new ArrayList<>();
        try {
            String result = restTemplate.getForObject(url, String.class);
            JsonNode root = new ObjectMapper().readTree(result);
            JsonNode memes = root.path("data").path("memes");

            for (JsonNode meme : memes) {
                Template template = new Template();
                template.setTemplateId(meme.path("id").asText());
                template.setName(meme.path("name").asText());
                template.setUrl(meme.path("url").asText());
                template.setWidth(meme.path("width").asInt());
                template.setHeight(meme.path("height").asInt());
                template.setBoxCount(meme.path("box_count").asInt());
                newTemplates.add(template);
            }
            templateRepository.saveAll(newTemplates);  // Save all at once for efficiency
        } catch (Exception e) {
            System.out.println("Error fetching or saving templates: " + e.getMessage());
        }
        return newTemplates;
    }

    private Template selectRandomTemplate(List<Template> templates) {
        if (!templates.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(templates.size());
            Template boxCount2;

            // just return a Template with boxcount of 2 but still a random one
            do {
                boxCount2 = templates.get(randomIndex);
                randomIndex = ThreadLocalRandom.current().nextInt(templates.size());
            } while (boxCount2.getBoxCount() != 2);

            return boxCount2;
            
        }
        return null;
    }


    //return template for user
    // I think we have a list of templates in the database and we want to fix on template for each round (chrigi)
    public Template getTemplateForUser(Long id) {
        //TODO implement
        return templateRepository.findById(id).orElse(null);
    }
}
