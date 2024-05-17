package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
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

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    private Lobby lobby;
    private List<Lobby> lobbyList;

    @BeforeEach
    public void setup() {
        lobby = new Lobby();
        lobby.setLobbyId(1L);
        lobby.setLobbyOwner(1L);
        lobby.setLobbyJoinCode("ABCD12");
        lobby.setGameActive(false);

        lobbyList = new ArrayList<>();
        lobbyList.add(lobby);
    }

    @Test
    public void createLobby_validInput_createsLobby() throws Exception {
        // given
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyOwner(1L);

        given(lobbyService.createLobby(Mockito.anyLong())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId().intValue())))
                .andExpect(jsonPath("$.lobbyOwner", is(lobby.getLobbyOwner().intValue())))
                .andExpect(jsonPath("$.lobbyJoinCode", is(lobby.getLobbyJoinCode())))
                .andExpect(jsonPath("$.gameActive", is(lobby.getGameActive())));
    }

    @Test
    public void getLobby_validInput_returnsLobby() throws Exception {
        // given
        given(lobbyService.getLobby(Mockito.anyLong())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbys/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId().intValue())))
                .andExpect(jsonPath("$.lobbyOwner", is(lobby.getLobbyOwner().intValue())))
                .andExpect(jsonPath("$.lobbyJoinCode", is(lobby.getLobbyJoinCode())))
                .andExpect(jsonPath("$.gameActive", is(lobby.getGameActive())));
    }

    @Test
    public void getAllLobbys_validInput_returnsLobbys() throws Exception {
        // given
        given(lobbyService.getLobbys()).willReturn(lobbyList);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbys")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lobbyId", is(lobby.getLobbyId().intValue())))
                .andExpect(jsonPath("$[0].lobbyOwner", is(lobby.getLobbyOwner().intValue())))
                .andExpect(jsonPath("$[0].lobbyJoinCode", is(lobby.getLobbyJoinCode())))
                .andExpect(jsonPath("$[0].gameActive", is(lobby.getGameActive())));
    }

    @Test
    public void retrieveLobbyToJoin_validInput_returnsLobby() throws Exception {
        // given
        LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
        lobbyPutDTO.setLobbyJoinCode("ABCD12");

        given(lobbyService.findLobbyByJoinCode(Mockito.anyString())).willReturn(lobby);
        doNothing().when(lobbyService).joinLobby(Mockito.anyLong(), Mockito.any(Lobby.class));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbys/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPutDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId", is(lobby.getLobbyId().intValue())))
                .andExpect(jsonPath("$.lobbyOwner", is(lobby.getLobbyOwner().intValue())))
                .andExpect(jsonPath("$.lobbyJoinCode", is(lobby.getLobbyJoinCode())))
                .andExpect(jsonPath("$.gameActive", is(lobby.getGameActive())));
    }

    @Test
    public void deleteLobby_validInput_deletesLobby() throws Exception {
        // given
        LobbyDeleteDTO lobbyDeleteDTO = new LobbyDeleteDTO();
        lobbyDeleteDTO.setLobbyId(1L);

        doNothing().when(lobbyService).deleteLobby(Mockito.anyLong(), Mockito.anyLong());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/lobbys/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyDeleteDTO));

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());
    }

    /*
     * Helper Method to convert DTO into a JSON string such that the input
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
