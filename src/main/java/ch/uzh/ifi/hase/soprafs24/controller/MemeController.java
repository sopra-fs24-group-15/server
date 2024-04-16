package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.MemeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memes")
public class MemeController {

    private final MemeService memeService;

    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/templates")
    public String getMemeTemplates() {
        return memeService.fetchMemes();
    }

    @PostMapping("/create")
    public String createMeme(@RequestParam String templateId, @RequestParam String textTop, @RequestParam String textBottom) {
        return memeService.createMeme(templateId, textTop, textBottom);
    }
}
