package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import ch.uzh.ifi.hase.soprafs24.repository.MemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;



class MemeServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private MemeRepository memeRepository;

    @InjectMocks
    private MemeService memeService;

    private Meme meme;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        meme = new Meme();
    }

    @Test   
    void createMeme_SuccessfulCreation_ReturnsMeme() {
        // Arrange
        String jsonResponse = "{\"data\":{\"url\":\"http://example.com/meme.jpg\", \"page_url\":\"http://example.com/page\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class))).thenReturn(responseEntity);
        when(memeRepository.save(any(Meme.class))).thenAnswer(i -> i.getArguments()[0]);
    
        // Act
        Meme result = memeService.createMeme(1L, 1L, List.of("Text1", "Text2"));
    
        // Assert
        assertNotNull(result);
        assertEquals("http://example.com/meme.jpg", result.getMemeURL());
        assertEquals("http://example.com/page", result.getPageUrl());
        assertEquals("MemeBattle2024", result.getCreator());
        assertEquals(0, result.getVotes());
    }

    @Test
    void createMeme_ApiFailure_ReturnsNull() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class))).thenReturn(responseEntity);
    
        // Act
        Meme result = memeService.createMeme(1L, 1L, List.of("Text1", "Text2"));
    
        // Assert
        assertNull(result);
    }

    @Test
    void whenApiResponseIsNotOk_thenReturnNull() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class))).thenReturn(responseEntity);
    
        // Act
        Meme result = memeService.createMeme(1L, 1L, List.of("Text1", "Text2"));
    
        // Assert
        assertNull(result);
    }


    @Test
    void testGetSetMemeId() {
        meme.setMemeId(1L);
        assertEquals(1L, meme.getMemeId());
    }

    @Test
    void testGetSetSuccess() {
        meme.setSuccess(true);
        assertTrue(meme.isSuccess());
    }

    @Test
    void testGetSetMemeURL() {
        String url = "http://example.com/meme.jpg";
        meme.setMemeURL(url);
        assertEquals(url, meme.getMemeURL());
    }

    @Test
    void testGetSetPageUrl() {
        String pageUrl = "http://example.com/page";
        meme.setPageUrl(pageUrl);
        assertEquals(pageUrl, meme.getPageUrl());
    }

    @Test
    void testGetSetCreator() {
        String creator = "user123";
        meme.setCreator(creator);
        assertEquals(creator, meme.getCreator());
    }

    @Test
    void testGetSetVotes() {
        int votes = 100;
        meme.setVotes(votes);
        assertEquals(votes, meme.getVotes());
    }

    

    
    
}