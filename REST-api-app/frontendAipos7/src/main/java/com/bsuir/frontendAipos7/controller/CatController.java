package com.bsuir.frontendAipos7.controller;

import com.bsuir.frontendAipos7.dto.CatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class CatController {
    private final RestTemplate restTemplate;

    @Value("${backend.url}")
    private String backendUrl;

    @GetMapping("/add")
    public String createCatForm() {
        return "create_cat_form";
    }

    @PostMapping("/add")
    public String addCat(@ModelAttribute CatDto catDto, Model model) {
        String url = backendUrl + "/api/add";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(catDto), String.class);

            log.info("HTTP Status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.CREATED) {
                log.info("Cat added successfully.");
                return "redirect:/cats";
            } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
                log.warn("Cat already exists.");
                return "redirect:/error";
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.warn("Invalid data for cat.");
                return "redirect:/error";
            }
        } catch (RestClientException e) {
            log.error("HTTP request failed: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
        }

        return "redirect:/error";
    }

    @GetMapping("/cats")
    public String getCats(Model model) {
        String url = backendUrl + "/api/all";
        ResponseEntity<List<CatDto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CatDto>>() {
                });
        log.info("HTTP Status: {}", response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("cats", response.getBody());
        }
        return "cats";
    }

    @GetMapping("/edit/{id}")
    public String editCatForm(@PathVariable Long id, Model model) {
        String url = backendUrl + "/api?id=" + id;

        try {
            ResponseEntity<CatDto> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<CatDto>() {
                    });
            log.info("HTTP Status: {}", response.getStatusCode());
            model.addAttribute("cat", response.getBody());
            return "edit_cat_form";
        } catch (RestClientException e) {
            log.error("Failed to retrieve cat with id '{}': {}. HTTP Status: {}", id, e.getMessage(), e.getCause());
            return "redirect:/error";
        }
    }

    @PostMapping("/edit")
    public String updateCat(@ModelAttribute CatDto catDto) {
        String url = backendUrl + "/api/update";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT,
                    new HttpEntity<>(catDto), String.class);

            log.info("HTTP Status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Cat with id '{}' updated successfully.", catDto.getId());
                return "redirect:/cats";
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Cat with id '{}' not found.", catDto.getId());
                return "redirect:/error";
            }
        } catch (RestClientException e) {
            log.error("HTTP request failed: {}", e.getMessage());
        }

        return "redirect:/error";
    }

    @PostMapping("/delete/{id}")
    public String deleteCat(@PathVariable Long id) {
        String url = backendUrl + "/api?id=" + id;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

            log.info("HTTP Status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Cat with id '{}' deleted successfully.", id);
                return "redirect:/cats";
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Cat with id '{}' not found.", id);
                return "redirect:/error";
            }
        } catch (RestClientException e) {
            log.error("HTTP request failed: {}", e.getMessage());
        }

        return "redirect:/error";
    }
}

