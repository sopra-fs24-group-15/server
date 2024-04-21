package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.service.MemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lobbys/{lobbyId}")
public class MemeController {

    private final MemeService memeService;

    @Autowired
    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/meme/{userId}")
    public ResponseEntity<?> getMeme(@PathVariable Long lobbyId, @PathVariable Long userId,@PathVariable List<String> texts ) {
        String memeURL = memeService.createMeme(lobbyId, userId, texts);
        if (memeURL != null) {
            return ResponseEntity.ok(memeURL);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User or lobby not found or already submitted");
        }
    }


    @PostMapping("/meme/{userId}")
    public ResponseEntity<?> createMeme(@PathVariable Long lobbyId, @PathVariable Long userId, @RequestBody List<String> texts) {
        String memeUrl = memeService.createMeme(lobbyId, userId, texts);
        if (memeUrl != null) {
            return ResponseEntity.ok(memeUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserId for meme creation not found");
        }
    }
}