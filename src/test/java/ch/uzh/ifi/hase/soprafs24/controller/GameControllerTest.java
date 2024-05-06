package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs24.service.GameService;


@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyRepository lobbyRepository;

    @Test
    public void createGame_validInput_createsGame() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setScores(new HashMap<>());
        game.setTotalRounds(5);
        game.setGameMode(GameMode.BASIC);
        game.setTimer(1);

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setTimer(1);
        gamePutDTO.setTotalRounds(5);

        given(gameService.createGame(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(GameMode.class), Mockito.anyInt())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbys/1/settings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePutDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRounds", is(game.getTotalRounds())))
                .andExpect(jsonPath("$.timer", is(game.getTimer())));
    }

 
    @Test
    public void createGame_invalidInput_throwsException() throws Exception {
        // given
        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setTimer(1);
        gamePutDTO.setTotalRounds(5);

        given(gameService.createGame(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(GameMode.class), Mockito.anyInt())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request body could not be created."));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbys/1/settings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePutDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void startGame_validInput_startsGame() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setScores(new HashMap<>());
        game.setTotalRounds(5);
        game.setGameMode(GameMode.BASIC);
        game.setTimer(1);

        //given(gameService.startGame(Mockito.anyLong(), Mockito.anyLong())).willReturn();

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbys/1/start/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }
    
    

    /*
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}