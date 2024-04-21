package ch.uzh.ifi.hase.soprafs24.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class MemeServiceTest {

    @InjectMocks
    private MemeService memeService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /*  // TODO (chrigi): Fix this test if yoou now what the returns are. Is it always a different URL?
    @Test
    public void createMeme_returnsMemeUrl() {
        // Given
        String jsonResponse = "{\"data\":{\"url\":\"https://i.imgflip.com/1bij.jpg\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn(responseEntity);

        List<String> texts = Arrays.asList("text1", "text2");

        // When
        String memeUrl = memeService.createMeme(1L, 1L, texts);

        // Then
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
        assertEquals("https://i.imgflip.com/1bij.jpg", memeUrl);
    }
    */
}