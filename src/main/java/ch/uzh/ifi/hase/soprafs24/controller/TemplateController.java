package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.service.TemplateService;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;


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
    private UserService userService;


    //TODO save template first in game service to round and then return it from round
    @GetMapping("/template/{userId}")
    public ResponseEntity<Template> fetchTemplate(@PathVariable Long lobbyId, @PathVariable Long userId) {
        Lobby lobby = lobbyService.getLobby(lobbyId);
        User user = userService.getUser(userId);

        if (lobby == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Template template = templateService.getTemplateForUser();
        if (template != null) {
            return ResponseEntity.ok(template);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    }
