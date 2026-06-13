package com.indexforge.search.service;

import com.indexforge.search.dto.AnimalResponse;
import com.indexforge.search.entity.Animal;
import com.indexforge.search.exception.AnimalNotFoundException;
import com.indexforge.search.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for reading animal documents from the database.
 * This is a read-only service. Data is populated by an external ingestion pipeline.
 */
@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    /**
     * Retrieves all animals.
     *
     * @return list of all animal responses
     */
    @Transactional(readOnly = true)
    public List<AnimalResponse> getAllAnimals() {
        return animalRepository.findAllByOrderByNameAsc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Retrieves an animal by its ID.
     *
     * @param id the animal ID
     * @return the animal response
     * @throws AnimalNotFoundException if animal is not found
     */
    @Transactional(readOnly = true)
    public AnimalResponse getAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));
        return mapToResponse(animal);
    }

    private AnimalResponse mapToResponse(Animal animal) {
        return AnimalResponse.builder()
                .id(animal.getId())
                .name(animal.getName())
                .height(animal.getHeight())
                .weight(animal.getWeight())
                .color(animal.getColor())
                .lifespan(animal.getLifespan())
                .diet(animal.getDiet())
                .habitat(animal.getHabitat())
                .predators(animal.getPredators())
                .averageSpeed(animal.getAverageSpeed())
                .countriesFound(animal.getCountriesFound())
                .description(animal.getDescription())
                .wikipediaSummary(animal.getWikipediaSummary())
                .createdAt(animal.getCreatedAt())
                .build();
    }
}