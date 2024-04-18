package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ch.uzh.ifi.hase.soprafs24.repository.MemeRepository;
import ch.uzh.ifi.hase.soprafs24.entity.Template;



@Service
public class TemplateService {

    private final RestTemplate restTemplate;

    @Autowired
    public TemplateService(RestTemplate restTemplate, MemeRepository memeRepository) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> fetchTemplate() {
        String url = "https://api.imgflip.com/get_memes";
        try {
            String result = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException e) {
            //TODO return message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<String> createTemplate(String templateId, String textTop, String textBottom) {
        String url = "https://api.imgflip.com/caption_image";
        String requestBody = "template_id=" + templateId +
                "&username=" + "MemeBattle2024" +
                "&password=" + "MemeBattle" +
                "&text0=" + textTop +
                "&text1=" + textBottom;
        try {
            String result = restTemplate.postForObject(url, requestBody, String.class);

            // Create a new Template object and populate it with the meme data
            Template template = new Template();
            template.setTemplateId(templateId);
            template.setTextTop(textTop);
            template.setTextBottom(textBottom);

            // Save the Template object to the database
            templateRepository.save(template);

            return ResponseEntity.ok(result);
        } catch (HttpClientErrorException e) {
            //TODO return message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
