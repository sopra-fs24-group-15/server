package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.service.TemplateService;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Round;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/lobby/{lobbyId}")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameService gameService;


    //TODO save template first in game service to round and then return it from round (GS)
    @GetMapping("/template")
    public ResponseEntity<Template> fetchTemplate(@PathVariable Long lobbyId) {
        Game game = gameService.getGame(lobbyId);
        Round round = game.getRound();
        Template template = round.getTemplate();
        return ResponseEntity.status(HttpStatus.OK).body(template);
    }
    }
