package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.entity.Round;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.rest.dto.MemePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
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

    @Autowired
    private TemplateService templateService;

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
    @PutMapping("/lobbys/{lobbyId}/meme/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void saveMeme(@RequestBody MemePutDTO memePutDTO, @PathVariable("userId") Long userId, @PathVariable("lobbyId") Long lobbyId, String textTop, String textBottom) {
        // convert API user to internal representation
        Meme memeInput = DTOMapper.INSTANCE.convertMemePutDTOtoEntity(memePutDTO);
        Long userIdInput = memeInput.getUserId();
        Long lobbyIdInput = memeInput.getMemeId();
        String TextTop = memePutDTO.getTextTop();
        String TextBottom = memePutDTO.getTextBottom();
        List<String> texts = new ArrayList<>();
        texts.add(textTop);
        texts.add(textBottom);

        // save Meme in Service (jana)
        Meme meme = memeService.saveMeme(lobbyIdInput, userIdInput, texts);
    }
}