package com.bsuir.backendAipos7.controller;

import com.bsuir.backendAipos7.service.CatService;
import com.bsuir.backendAipos7.service.dto.CatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CatController {
    private final CatService catService;

    @PostMapping("/add")
    public ResponseEntity<String> addCat(@RequestBody CatDto catDto) {
        log.info("Operation: Add a new cat - Started");
        try {
            CatDto savedCat = catService.create(catDto);
            log.info("Operation: Add a new cat - Completed, Status: OK (201), Cat: {}", savedCat);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cat added: " + savedCat);
        } catch (Exception e) {
            log.error("Operation: Add a new cat - Failed, Status: Internal Server Error (500)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add cat");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CatDto>> getAll() {
        log.info("Operation: Fetch all cats - Started");
        try {
            List<CatDto> cats = catService.getAll();

            if (cats.isEmpty()) {
                log.info("Operation: Fetch all cats - Completed, Status: No Content (204)");
                return ResponseEntity.noContent().build();
            }

            log.info("Operation: Fetch all cats - Completed, Status: OK (200), Total: {}", cats.size());
            return ResponseEntity.ok(cats);

        } catch (Exception e) {
            log.error("Operation: Fetch all cats - Failed, Status: Internal Server Error (500)", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping
    public ResponseEntity<CatDto> getCat(@RequestParam Long id) {
        log.info("Operation: Fetch cat by ID - Started, ID: {}", id);
        try {
            CatDto cat = catService.getById(id);
            log.info("Operation: Fetch cat by ID - Completed, Status: OK (200), Cat: {}", cat);
            return ResponseEntity.ok(cat);
        } catch (RuntimeException e) {
            log.warn("Operation: Fetch cat by ID - Not Found, Status: Not Found (404), ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Operation: Fetch cat by ID - Failed, Status: Internal Server Error (500), ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCat(@RequestParam Long id) {
        log.info("Operation: Delete cat by ID - Started, ID: {}", id);
        try {
            if (catService.getById(id) == null) {
                log.warn("Operation: Delete cat by ID - Not Found, Status: Not Found (404), ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cat not found");
            }

            catService.delete(id);
            log.info("Operation: Delete cat by ID - Completed, Status: OK (200), ID: {}", id);
            return ResponseEntity.ok("Cat deleted");
        } catch (Exception e) {
            log.error("Operation: Delete cat by ID - Failed, Status: Internal Server Error (500), ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete cat");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> changeCat(@RequestBody CatDto catDto) {
        log.info("Operation: Update cat - Started, Cat: {}", catDto);
        try {
            if (catService.getById(catDto.getId()) == null) {
                log.warn("Operation: Update cat - Not Found, Status: Not Found (404), ID: {}", catDto.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such cat exists");
            }

            CatDto updatedCat = catService.update(catDto);
            log.info("Operation: Update cat - Completed, Status: OK (200), Cat: {}", updatedCat);
            return ResponseEntity.ok("Cat updated: " + updatedCat);
        } catch (Exception e) {
            log.error("Operation: Update cat - Failed, Status: Internal Server Error (500), Cat: {}", catDto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update cat");
        }
    }
}