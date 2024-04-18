
package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.repository.MemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




@Service
public class MemeService {

    private final RestTemplate restTemplate;
    private final MemeRepository memeRepository;

    @Autowired
    public MemeService(RestTemplate restTemplate, MemeRepository memeRepository) {
        this.restTemplate = restTemplate;
        this.memeRepository = memeRepository;
    }

    public Meme saveMeme(Meme meme) {
        return memeRepository.save(meme);
    }

    public boolean handleSuccessResponse(Meme meme) {
        return true;
    }

    public boolean handleErrorResponse(Meme meme) {
        return false;
    }

}