package ch.uzh.ifi.hase.soprafs24.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.repository.TemplateRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;



public class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TemplateRepository templateRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void fetchTemplate_returnsTemplates() {
        // TODO: write test (chrigi)
    }

    @Test
    public void getTemplateForUser_returnsTemplate() {
        // TODO: write test (chrigi) now everithing is accepted
        Template template = new Template();
        when(templateRepository.getOne(anyLong())).thenReturn(template);

        // When
        Template result = templateService.getTemplateForUser();

        // Then
        verify(templateRepository, times(1)).getOne(anyLong());
        assertEquals(template, result);
    }
}