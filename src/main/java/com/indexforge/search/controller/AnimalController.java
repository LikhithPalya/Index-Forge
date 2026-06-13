package com.indexforge.search.controller;

import com.indexforge.search.dto.AnimalResponse;
import com.indexforge.search.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for animal document retrieval.
 */
@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    /**
     * Retrieves all animals.
     *
     * GET /api/v1/animals
     */
    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAllAnimals() {
        List<AnimalResponse> response = animalService.getAllAnimals();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves an animal by ID.
     *
     * GET /api/v1/animals/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimal(@PathVariable Long id) {
        AnimalResponse response = animalService.getAnimal(id);
        return ResponseEntity.ok(response);
    }
}