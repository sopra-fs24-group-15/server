package ch.uzh.ifi.hase.soprafs24.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Voting;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.MemeRepository;

@Service
public class MemeService {

    private final RestTemplate restTemplate;
    private final MemeRepository memeRepository;

    @Autowired
    public MemeService(RestTemplate restTemplate, MemeRepository memeRepository) {
        this.restTemplate = restTemplate;
        this.memeRepository = memeRepository;
    }

    public String fetchMemes() {
        String url = "https://api.imgflip.com/get_memes";
        try {
            String result = restTemplate.getForObject(url, String.class);
            return result;
        } catch (HttpClientErrorException e) {
            //TODO return message
            return null;
        }
    }

    public String createMeme(String templateId, String textTop, String textBottom) {
        String url = "https://api.imgflip.com/caption_image";
        String requestBody = "template_id=" + templateId +
                "&username=" + "yourUsername" +
                "&password=" + "yourPassword" +
                "&text0=" + textTop +
                "&text1=" + textBottom;
        try {
            String result = restTemplate.postForObject(url, requestBody, String.class);
            return result;
        } catch (HttpClientErrorException e) {
            //TODO return message
            return null;
        }
    }
}
