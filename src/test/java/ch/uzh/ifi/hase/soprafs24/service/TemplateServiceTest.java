package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TemplateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateService templateService;
    private Template template;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        template = new Template();
    }
    
    @Test
    public void fetchTemplate_Successful_ReturnsTemplates() throws Exception {
        // Arrange
        String json = "{\"data\":{\"memes\":[{\"id\":\"123\",\"name\":\"Meme1\",\"url\":\"http://example.com/meme1\",\"width\":500,\"height\":600,\"box_count\":2}]}}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        // Act
        List<Template> templates = templateService.fetchTemplate();

        // Assert
        assertNotNull(templates);
        assertFalse(templates.isEmpty());
        assertEquals(1, templates.size());
        Template template = templates.get(0);
        assertEquals("123", template.getTemplateId());
        assertEquals("Meme1", template.getName());
        assertEquals("http://example.com/meme1", template.getUrl());
        assertEquals(500, template.getWidth());
        assertEquals(600, template.getHeight());
        assertEquals(2, template.getBoxCount());

        verify(templateRepository, times(1)).save(any(Template.class)); // Ensure each template is saved
    }

    @Test
    public void fetchTemplate_Failure_IOException() throws Exception {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("Failed to fetch templates"));

        // Act
        List<Template> templates = templateService.fetchTemplate();

        // Assert
        assertNull(templates);
    }

    @Test
    public void getTemplateForUser_TemplateExists_ReturnsTemplate() {
        // Arrange
        Template expectedTemplate = new Template();
        expectedTemplate.setId(1L);
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.of(expectedTemplate));

        // Act
        Template result = templateService.getTemplateForUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTemplate, result);
    }

    @Test
    public void getTemplateForUser_TemplateNotFound_ReturnsNull() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act
        Template result = templateService.getTemplateForUser(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetSetId() {
        Long id = 1L;
        template.setId(id);
        assertEquals(id, template.getId());
    }

    @Test
    void testGetSetTopic() {
        String topic = "Funny";
        template.setTopic(topic);
        assertEquals(topic, template.getTopic());
    }

    @Test
    void testGetSetTextTop() {
        String textTop = "What if I told you";
        template.setTextTop(textTop);
        assertEquals(textTop, template.getTextTop());
    }

    @Test
    void testGetSetTextBottom() {
        String textBottom = "Memes are great";
        template.setTextBottom(textBottom);
        assertEquals(textBottom, template.getTextBottom());
    }


}