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
import java.util.Random;
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

    private final String[] themes = {
        "Adventure", "Romance", "Mystery", "Horror", "Science Fiction", "Fantasy", "Historical",
        "Comedy", "Drama", "Action", "Thriller", "Western", "Documentary", "Biography",
        "Musical", "War", "Sports", "Political", "Crime", "Family", "Supernatural", "Espionage",
        "Teen", "Medical", "Legal", "Sitcom", "Classic", "Reality", "Game Show", "Travel",
        "Cooking", "Nature", "Science", "History", "Technology", "Mythology", "Animation",
        "Children", "Educational", "News", "Art", "Lifestyle", "Magic", "Military", "Philosophical",
        "Psychological", "Surreal", "Post-Apocalyptic", "Dystopian", "Cyberpunk", "Steampunk",
        "Space Opera", "High Fantasy", "Epic", "Urban Fantasy", "Hard Science Fiction", "Literary",
        "Detective", "Noir", "Zombie", "Vampire", "Gothic", "Time Travel", "Superhero", "Martial Arts",
        "Parody", "Satire", "Folklore", "Fairy Tale", "Love Story", "Elderly", "Youth", "Coming of Age",
        "Patriotic", "Religious", "Spiritual", "Tragicomedy", "Slice of Life", "Anthology", "Puppetry",
        "Fan Fiction", "Magical Realism", "Alternate History", "Prehistoric", "Colonial", "Victorian",
        "Regency", "Elizabethan", "Renaissance", "Futuristic", "Alien Invasion", "Survival", "Wilderness",
        "Oceanic", "Desert", "Urban", "Rural", "Virtual Reality", "Augmented Reality", "MMORPG", "Interactive"
    };

    public String getTheme(){
        Random random = new Random();
        int index = random.nextInt(themes.length);
        return themes[index];
    }
}
