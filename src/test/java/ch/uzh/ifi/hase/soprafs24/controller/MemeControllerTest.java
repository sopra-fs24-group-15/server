package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemeController.class)
public class MemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemplateService templateService;

    @MockBean
    private GameService gameService;

    @Test
    public void testFetchTemplate_Success() throws Exception {
        Game game = new Game();
        Round round = new Round();
        Template template = new Template();
        template.setId(1L);
        template.setTemplateId("1");
        template.setName("Mock Template");
        template.setUrl("https://www.example.com");
        template.setWidth(100);
        template.setHeight(100);
        template.setBoxCount(2);
        template.setTopic("Mock Topic");
        round.setTemplate(template);
        game.setRound(round);

        given(gameService.getGame(1L)).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/lobbys/1/templates")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(template.getUrl()));
    }

    


    @Test
    public void testIsUserWithLowestScore_Success() throws Exception {
        given(gameService.isUserWithLowestScore(1L, 1L)).willReturn(true);

        MockHttpServletRequestBuilder getRequest = get("/lobbys/1/users/1/lowest-score")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMemes_Success() throws Exception {
        Meme meme1 = new Meme();
        meme1.setMemeId(1L);
        meme1.setMemeURL("https://www.example.com/meme1");
        meme1.setUserId(1L);
        
        Meme meme2 = new Meme();
        meme2.setMemeId(2L);
        meme2.setMemeURL("https://www.example.com/meme2");
        meme2.setUserId(1L);
        
        List<Meme> memes = Arrays.asList(meme1, meme2);

        given(gameService.getMemes(1L, 1L)).willReturn(memes);

        MockHttpServletRequestBuilder getRequest = get("/lobbys/1/memes/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memeURL").value(meme1.getMemeURL()))
                .andExpect(jsonPath("$[1].memeURL").value(meme2.getMemeURL()));
    }
}
