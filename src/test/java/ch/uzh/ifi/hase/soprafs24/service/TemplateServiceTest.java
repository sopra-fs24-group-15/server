package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;

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
        template.setId(1L);
        template.setTemplateId("1");
        template.setName("Test Template");
        template.setUrl("http://test.url");
        template.setWidth(100);
        template.setHeight(100);
        template.setBoxCount(2);
        template.setTopics(List.of("Test", "Template", "Topics"));
    }

    @Test
    public void fetchTemplate_NoTemplatesInRepo_FetchesFromAPI() {
        // Arrange
        when(templateRepository.findAll()).thenReturn(Collections.emptyList());
        String apiResponse = "{\"data\":{\"memes\":[{\"id\":\"1\",\"name\":\"Test Template\",\"url\":\"http://test.url\",\"width\":100,\"height\":100,\"box_count\":2}]}}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(apiResponse);

        // Act
        Template fetchedTemplate = templateService.fetchTemplate();

        // Assert
        assertNotNull(fetchedTemplate);
        assertEquals("Test Template", fetchedTemplate.getName());
        verify(templateRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void fetchTemplate_TemplatesInRepo_ReturnsRandomTemplate() {
        // Arrange
        List<Template> templates = new ArrayList<>();
        templates.add(template);
        when(templateRepository.findAll()).thenReturn(templates);

        // Act
        Template fetchedTemplate = templateService.fetchTemplate();

        // Assert
        assertNotNull(fetchedTemplate);
        assertEquals("Test Template", fetchedTemplate.getName());
        verify(templateRepository, never()).saveAll(anyList());
    }

    @Test
    public void fetchTemplate_APIException_ReturnsNull() {
        // Arrange
        when(templateRepository.findAll()).thenReturn(Collections.emptyList());
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("Failed to fetch templates"));

        // Act
        Template template = templateService.fetchTemplate();

        // Assert
        assertNull(template);
    }

    @Test
    public void getTemplateForUser_TemplateExists_ReturnsTemplate() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(Optional.of(template));

        // Act
        Optional<Template> foundTemplate = templateRepository.findById(1L);

        // Assert
        assertTrue(foundTemplate.isPresent());
        assertEquals(template, foundTemplate.get());
    }

    @Test
    public void getTemplateForUser_TemplateNotFound_ReturnsEmpty() {
        // Arrange
        when(templateRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Template> foundTemplate = templateRepository.findById(1L);

        // Assert
        assertTrue(foundTemplate.isEmpty());
    }

    @Test
    void testGetSetId() {
        Long id = 1L;
        template.setId(id);
        assertEquals(id, template.getId());
    }

    @Test
    void selectRandomTemplate_TemplatesAvailable_ReturnsTemplateWithBoxCountLessThanOrEqual4() {
        // Arrange
        List<Template> templates = new ArrayList<>();
        templates.add(template);
        Template templateWithBoxCount5 = new Template();
        templateWithBoxCount5.setBoxCount(5);
        templates.add(templateWithBoxCount5);

        // Act
        Template selectedTemplate = templateService.selectRandomTemplate(templates);

        // Assert
        assertNotNull(selectedTemplate);
        assertTrue(selectedTemplate.getBoxCount() <= 4);
    }

    @Test
    void selectRandomTemplate_NoTemplates_ReturnsNull() {
        // Act
        Template selectedTemplate = templateService.selectRandomTemplate(Collections.emptyList());

        // Assert
        assertNull(selectedTemplate);
    }
}
