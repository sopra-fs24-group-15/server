package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

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
    }
    
    //TODO change this test (chrigi)
    /* 
    @Test
    public void fetchTemplate_Successful_ReturnsTemplates() throws Exception {
        
        Template template = templateService.selectRandomTemplate();

        System.out.println("start");
        System.out.println(template.getName());
        System.out.println("end");

        // Assert
        assertNotNull(template);
        
        //verify(templateRepository, times(100)).save(any(Template.class)); // Ensure each template is saved
    }
    */


    /* 
    @Test
    public void fetchTemplate_Failure_IOException() throws Exception {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("Failed to fetch templates"));

        // Act
        List<Template> templates = templateService.fetchTemplate();

        // Assert
        assertNull(templates);
    }

    */

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

   



}