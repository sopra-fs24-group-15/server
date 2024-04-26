package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TemplateGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;



@RestController
@RequestMapping
public class MemeController {

    private final TemplateService templateService;

    private final GameService gameService;

    // Autowiring the TemplateService through the constructor
    public MemeController(TemplateService templateService, GameService gameService) {
        this.templateService = templateService;
        this.gameService = gameService;
    }

    @GetMapping("/lobby/{lobbyId}/template")
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
}