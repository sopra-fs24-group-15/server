package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.service.MemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;



import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/lobby/{lobbyId}")
public class MemeController {

    private final MemeService memeService;
    private final LobbyService lobbyService;
    private final UserService userService;

    public MemeController(MemeService memeService, LobbyService lobbyService, UserService userService) {
        this.memeService = memeService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    @GetMapping("/meme/{userId}")
    public ResponseEntity<Meme> getMeme(@PathVariable Long lobbyId, @PathVariable Long userId) {
        // implementation...
        return null;
    }

    @GetMapping("/memes/{userId}")
    public ResponseEntity<Map<Long, Meme>> getMemes(@PathVariable Long lobbyId, @PathVariable Long userId) {
        return null;
    }

    @PostMapping("/meme/{userId}")
    public ResponseEntity<Meme> createMeme(@PathVariable Long lobbyId, @PathVariable Long userId, @RequestBody List<String> texts) {
        // implementation...


        return null;
    }
}