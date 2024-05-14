package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
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

    private List<String> getRandomTopicsList(int count) {
        List<String> topicsList = new ArrayList<>(List.of(topics));
        Collections.shuffle(topicsList);
        return topicsList.subList(0, count);
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

                template.setTopics(getRandomTopicsList(3));
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
            
            do {
                boxCount2 = templates.get(randomIndex);
                randomIndex = ThreadLocalRandom.current().nextInt(templates.size());
            } while (boxCount2.getBoxCount() > 4);

            return boxCount2;
            
        }
        return null;
    }


    private final String[] topics = {
        "Adventure", "Romance", "Mystery", "Horror", "Science Fiction", "Fantasy", "Historical",
        "Comedy", "Drama", "Action", "Thriller", "Western", "Documentary", "Biography",
        "Musical", "War", "Sports", "Political", "Crime", "Family", "Espionage",
        "Teen", "Medical", "Legal", "Sitcom", "Classic", "Reality", "Game Show", "Travel",
        "Cooking", "Nature", "Science", "History", "Technology", "Mythology", "Animation",
        "Children", "Educational", "News", "Art", "Lifestyle", "Magic", "Military", "Philosophical",
        "Psychological", "Post-Apocalyptic", "Cyberpunk", "Steampunk",
        "Space Opera", "High Fantasy", "Epic", "Hard Science Fiction", "Literary",
        "Detective", "Vampire", "Gothic", "Time Travel", "Superhero",
        "Parody", "Satire", "Fairy Tale", "Love Story", "Elderly", "Youth", "Coming of Age",
        "Patriotic", "Religious", "Spiritual", "Survival", "Wilderness",
        "Strange phobias", "Awkward family photos", "Weird food combinations", "Celebrity look-alikes", "Silly animal behaviors",
        "Epic fail moments", "Unusual talents", "Funny autocorrect mishaps", "Embarrassing moments", "Awkward first dates",
        "Hilarious typos", "Strange fashion trends", "Overheard conversations", "Misheard song lyrics", "Bizarre inventions",
        "Awkward text messages", "Funny pet names", "Hilarious movie quotes", "Kids say the darndest things", "Office pranks",
        "Bad haircuts", "Accidental texts", "Weird dreams", "Funny mugshots", "Crazy conspiracy theories", "Unexpected plot twists",
        "Silly superstitions", "Awkward wedding moments", "Hilarious yearbook quotes", "Funny license plates", "Strange laws",
        "Misadventures in cooking", "Funny product reviews", "Awkward family reunions", "Ridiculous workout routines",
        "Unfortunate tattoos", "Hilarious headlines", "Funny interview stories", "Strange hobbies", "Silly baby names",
        "Awkward encounters", "Prank phone calls", "Funny pet costumes", "Hilarious school projects", "Awkward family traditions",
        "Embarrassing social media posts", "Strange things found in thrift stores", "Funny holiday traditions", 
        "Awkward moments in history", "Unintentionally funny signs", "Hilarious greeting cards", "Awkward handshakes",
        "Weird beauty treatments", "Silly voice impressions", "Strange urban legends", "Funny pickup lines",
        "Awkward public transportation stories", "Misadventures in online dating", "Hilarious travel mishaps",
        "Funny language translations", "Awkward moments with in-laws", "Hilarious sports bloopers", "Strange habits",
        "Funny tourist photos", "Awkward elevator rides", "Hilarious customer service stories", "Funny pet videos",
        "Embarrassing wardrobe malfunctions", "Awkward party stories", "Weird school rules", "Hilarious autocorrect fails",
        "Funny sibling stories", "Strange encounters with wildlife", "Awkward moments on live TV", "Funny things kids believe",
        "Unusual holiday decorations", "Silly conspiracy theories", "Hilarious product names", "Awkward gym moments", "Strange superstitions",
        "Funny senior quotes", "Weird things overheard in public", "Awkward breakups", "Hilarious office stories",
        "Embarrassing moments with parents", "Silly spelling mistakes", "Funny job applications", "Awkward bathroom encounters",
        "Hilarious roommate stories", "Strange rituals", "Funny voice messages", "Awkward photo bombs", "Weird dreams that came true",
        "Hilarious pet antics", "Embarrassing autocorrects", "Silly fashion fails", "Funny travel stories", "Awkward moments in sports",
        "Hilarious cooking disasters", "Weird things people believe", "Awkward moments at school", "Funny birthday stories", "Strange coincidences",
        "Hilarious pet fails", "Awkward moments at work", "Funny shopping experiences", "Embarrassing mispronunciations", "Silly dance moves",
        "Weird food cravings", "Hilarious graduation moments", "Awkward public speaking moments", "Funny holiday mishaps", "Strange celebrity rumors",
        "Hilarious parking fails", "Awkward moments at the gym", "Funny pet tricks", "Embarrassing moments on stage",
        "Silly things people do when they're bored", "Weird things found on the internet", "Hilarious wedding speeches", "Awkward moments in restaurants",
        "Funny things pets do", "Strange traditions from around the world", "Hilarious DIY fails", "Embarrassing moments in public",
        "Silly things people say when tired", "Funny moments in live performances", "Awkward moments with strangers",
        "Hilarious moments in history", "Strange pet behaviors", "Funny things people collect", "Awkward school presentations",
        "Hilarious things kids write", "Weird things people do in their sleep", "Funny moments on public transportation",
        "Embarrassing moments caught on camera", "Silly pet habits", "Hilarious pranks gone wrong", "Awkward moments at the doctor's office",
        "Funny pet interactions", "Strange things people believe in", "Hilarious moments in politics", "Awkward moments at family gatherings",
        "Funny things overheard at work", "Embarrassing moments in sports", "Silly mistakes in movies", "Weird things found in the attic",
        "Hilarious things kids draw", "Awkward moments at the dentist", "Funny pet photos", "Strange urban myths", "Hilarious moments in TV shows",
        "Embarrassing moments on vacation", "Silly things people do when nervous", "Funny things overheard in stores", "Awkward moments at parties",
        "Hilarious things pets destroy", "Strange things found in nature", "Funny moments in reality TV", "Awkward moments at the airport",
        "Hilarious school stories", "Embarrassing moments in relationships", "Silly things people do when scared", "Funny things overheard in cafes",
        "Awkward moments at the beach", "Hilarious pet expressions", "Strange things found in the backyard",
        "Funny moments in sports games", "Embarrassing moments with teachers", "Silly things people do for love", "Funny things overheard on the phone",
        "Awkward moments in stores", "Hilarious pet reactions", "Strange things found in the garage", "Funny moments in parades",
        "Embarrassing moments in the kitchen", "Silly things people do when excited", "Funny things overheard at the park",
        "Awkward moments in elevators", "Hilarious moments in video games", "Strange things found at the beach", "Funny moments in competitions",
        "Embarrassing moments with friends", "Silly things people do when angry", "Funny things overheard in the gym", "Awkward moments at concerts",
        "Hilarious pet habits", "Strange things found in the car", "Funny moments in talent shows", "Embarrassing moments in public speaking",
        "Silly things people do when surprised", "Funny things overheard at the airport", "Awkward moments in cafes", "Hilarious moments in interviews",
        "Strange things found on the road", "Funny moments in games", "Embarrassing moments at work", "Silly things people do when happy", "Funny things overheard in class", "Awkward moments with neighbors"
    };
}
