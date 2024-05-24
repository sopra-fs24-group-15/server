package ch.uzh.ifi.hase.soprafs24.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

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

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        User user = new User();
        user.setUsername("firstname@lastname");

        List<User> allUsers = Collections.singletonList(user);

        given(userService.getUsers()).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setLobbyOwner(true);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setLobbyOwner(true);

        given(userService.createUser(Mockito.anyString(), Mockito.anyBoolean())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void deleteUser_validId_noContent() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        MockHttpServletRequestBuilder deleteRequest = delete("/users/{id}", userId);

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
    }

    @Test
    public void getBestMeme_validId_returnMeme() throws Exception {
        Long userId = 1L;
        String bestMeme = "Best Meme URL";

        User user = new User();
        user.setUserId(userId);
        user.setBestMeme(bestMeme);

        given(userService.getUser(userId)).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/{id}/memes", userId);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(bestMeme)));
    }

    @Test
    public void updateProfilePicture_validId_noContent() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).updateProfilePicture(userId);

        MockHttpServletRequestBuilder putRequest = put("/users/{id}/profilepictures", userId);

        mockMvc.perform(putRequest).andExpect(status().isNoContent());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

    
    @Test
    public void UserDeleteDTOtestGettersAndSetters() {
        UserDeleteDTO userDeleteDTO = new UserDeleteDTO();
        
        Long userId = 1L;
        String username = "testUser";

        userDeleteDTO.setUserId(userId);
        userDeleteDTO.setUsername(username);

        assertEquals(userId, userDeleteDTO.getUserId());
        assertEquals(username, userDeleteDTO.getUsername());
    }

}
