package com.indexforge.search.config;

import com.indexforge.search.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Validates database connectivity and pgvector availability on application startup.
 * Logs the status of the search index (number of animals with embeddings).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthValidator {

    private final AnimalRepository animalRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void validateDatabaseOnStartup() {
        log.info("=== IndexForge Search - Startup Validation ===");

        // 1. Verify database connectivity
        try {
            long totalAnimals = animalRepository.count();
            log.info("[OK] PostgreSQL connected. Total animals: {}", totalAnimals);
        } catch (Exception e) {
            log.error("[FAIL] Cannot connect to PostgreSQL: {}", e.getMessage());
            return;
        }

        // 2. Verify pgvector extension is available
        try {
            int pgvectorCount = animalRepository.checkPgvectorExtension();
            if (pgvectorCount > 0) {
                log.info("[OK] pgvector extension is enabled");
            } else {
                log.error("[FAIL] pgvector extension is NOT installed");
            }
        } catch (Exception e) {
            log.error("[FAIL] Could not verify pgvector extension: {}", e.getMessage());
        }

        // 3. Check embedding coverage
        try {
            long withEmbeddings = animalRepository.countWithEmbeddings();
            long totalAnimals = animalRepository.count();
            log.info("[OK] Animals with embeddings: {}/{}", withEmbeddings, totalAnimals);

            if (withEmbeddings == 0) {
                log.warn("[WARN] No animals have embeddings. Semantic search will return no results.");
            }
        } catch (Exception e) {
            log.error("[FAIL] Could not check embedding coverage: {}", e.getMessage());
        }

        log.info("=== Startup Validation Complete ===");
    }
}