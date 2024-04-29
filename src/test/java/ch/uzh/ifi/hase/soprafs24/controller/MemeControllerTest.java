package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import ch.uzh.ifi.hase.soprafs24.service.TemplateService;


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
        // given
        Lobby lobby = new Lobby();
        lobby.setLobbyId(1L);

        Game game = new Game();
        game.setGameId(1L);

        Template template = new Template();
        template.setId(1L);
        template.setTemplateId("1");
        template.setName("Mock Template");
        template.setUrl("https://www.example.com");
        template.setWidth(100);
        template.setHeight(100);
        template.setBoxCount(2);
        template.setTopic("Mock Topic");
        template.setTextTop("Top Text");
        template.setTextBottom("Bottom Text");
        
        given(templateService.fetchTemplate()).willReturn(template);

        MockHttpServletRequestBuilder getRequest = get("/lobby/1/template")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", is(template.getUrl())));
    }

    

}
    