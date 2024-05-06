package ch.uzh.ifi.hase.soprafs24.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MemeGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TemplateGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.TemplateService;





@RestController
@RequestMapping
public class MemeController {

    private final TemplateService templateService;

    private final GameService gameService;

    private final Logger log = LoggerFactory.getLogger(MemeController.class);

    

    // Autowiring the TemplateService through the constructor
    public MemeController(TemplateService templateService, GameService gameService) {
        this.templateService = templateService;
        this.gameService = gameService;
    }


    @GetMapping("/lobbys/{lobbyId}/templates")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateGetDTO fetchTemplate(@PathVariable Long lobbyId) {
            Game game = gameService.getGame(lobbyId);
            Round round = game.getRound();
            Template template = round.getTemplate();
            
            if (template != null) {
                TemplateGetDTO templateDTO = DTOMapper.INSTANCE.convertEntityToTemplateGetDTO(template);
                return templateDTO;
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template not found");
            }
        }

    @PostMapping("/lobbys/{lobbyId}/memes/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setMeme(@PathVariable Long lobbyId, @PathVariable Long userId, @RequestBody String memeURL) {
        gameService.setMeme(lobbyId, userId, memeURL);
    }

    @GetMapping("/lobbys/{lobbyId}/memes/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MemeGetDTO> getMemes(@PathVariable Long lobbyId, @PathVariable Long userId) {
        
        List<Meme> memes =  gameService.getMemes(lobbyId, userId);

        List<MemeGetDTO> memeDTOs = new ArrayList<>();

        if (memes != null) {
            for (Meme meme : memes) {
                memeDTOs.add(DTOMapper.INSTANCE.convertEntityToMemeGetDTO(meme));
            }
        } else {
            // Handle the case where memes is null, perhaps log a message or return an empty list
            log.warn("No memes found for lobbyId {} and userId {}", lobbyId, userId);
        }

        return memeDTOs;
    }  
    
}