package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import ch.uzh.ifi.hase.soprafs24.rest.dto.TemplateGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;

import ch.uzh.ifi.hase.soprafs24.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;



@RestController
@RequestMapping
public class MemeController {

    private final TemplateService templateService;

    // Autowiring the TemplateService through the constructor
    public MemeController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/template")

    public ResponseEntity<TemplateGetDTO> fetchTemplate() {
        try {
            Template template = templateService.fetchTemplate();
            if (template != null) {
                TemplateGetDTO templateDTO = DTOMapper.INSTANCE.convertEntityToTemplateGetDTO(template);
                return ResponseEntity.ok(templateDTO);
            } else {
                System.out.println("No template found"); // Debug log
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Error fetching template: " + e.getMessage()); // Exception logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}