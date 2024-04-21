
package ch.uzh.ifi.hase.soprafs24.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;



@Service
@Transactional
public class MemeService {
    public String createMeme(Long lobbyId, Long userId, List<String> texts) {
        String templateId = "some-template-id"; // replace with actual template ID
        String username = "MemeBattle2024";
        String password = "MemeBattle";
        String text0 = texts.get(0);
        String text1 = texts.get(1);

        String url = "https://api.imgflip.com/caption_image";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("template_id", templateId);
        map.add("username", username);
        map.add("password", password);
        map.add("text0", text0);
        map.add("text1", text1);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String memeUrl = root.path("data").path("url").asText();
                return memeUrl;
            } catch (Exception e) {
                e.printStackTrace();
                return "could not get url";
            }
        } else {
            return "did not work";
        }
    }
}