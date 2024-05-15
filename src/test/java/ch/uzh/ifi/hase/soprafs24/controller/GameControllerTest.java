package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ScoreGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.GameService;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyRepository lobbyRepository;

    private Game game;
    private User user;
    private List<User> users;

    @BeforeEach
    public void setup() {
        game = new Game();
        game.setGameId(1L);
        game.setScores(new HashMap<>());
        game.setTotalRounds(5);
        game.setGameMode(GameMode.BASIC);
        game.setTimer(30);
        game.setCurrentRound(1);

        user = new User();
        user.setUserId(1L);
        user.setUsername("user1");

        users = new ArrayList<>();
        users.add(user);
    }

    @Test
    public void createGame_validInput_createsGame() throws Exception {
        // given
        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setTimer(30);
        gamePutDTO.setTotalRounds(5);
        gamePutDTO.setGameMode(GameMode.BASIC);

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
        gamePutDTO.setTimer(30);
        gamePutDTO.setTotalRounds(5);
        gamePutDTO.setGameMode(GameMode.BASIC);

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
        doNothing().when(gameService).startGame(Mockito.anyLong(), Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbys/1/start/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void startNextRound_validInput_startsNextRound() throws Exception {
        // given
        given(gameService.startNextRound(Mockito.anyLong())).willReturn(true);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbys/1/rounds/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void endRound_validInput_endsRound() throws Exception {
        // given
        doNothing().when(gameService).endRound(Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbys/1/rounds/end")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void vote_validInput_votes() throws Exception {
        // given
        doNothing().when(gameService).setVote(Mockito.anyLong(), Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbys/1/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1L));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getSubmittedVotes_validInput_returnsSubmittedVotes() throws Exception {
        // given
        given(gameService.getSubmittedVotes(Mockito.anyLong())).willReturn(3);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbys/1/votes")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(3)));
    }

    

    @Test
    public void getSettings_validInput_returnsSettings() throws Exception {
        // given
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);

        given(gameService.getGame(Mockito.anyLong())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbys/1/settings")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRounds", is(gameGetDTO.getTotalRounds())))
                .andExpect(jsonPath("$.gameMode", is(gameGetDTO.getGameMode().toString())))
                .andExpect(jsonPath("$.timer", is(gameGetDTO.getTimer())));
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
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
