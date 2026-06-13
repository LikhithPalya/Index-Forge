package com.indexforge.search.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing an animal document in the search index.
 * Source of truth is PostgreSQL. This service only reads from the database.
 */
@Entity
@Table(name = "animals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "height", length = 100)
    private String height;

    @Column(name = "weight", length = 100)
    private String weight;

    @Column(name = "color", length = 255)
    private String color;

    @Column(name = "lifespan", length = 100)
    private String lifespan;

    @Column(name = "diet", length = 255)
    private String diet;

    @Column(name = "habitat", columnDefinition = "TEXT")
    private String habitat;

    @Column(name = "predators", columnDefinition = "TEXT")
    private String predators;

    @Column(name = "average_speed", length = 100)
    private String averageSpeed;

    @Column(name = "countries_found", columnDefinition = "TEXT")
    private String countriesFound;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "wikipedia_summary", columnDefinition = "TEXT")
    private String wikipediaSummary;

    @Column(name = "search_document", columnDefinition = "TEXT")
    private String searchDocument;

    /**
     * Embedding vector for semantic similarity search.
     * Stored as pgvector type in PostgreSQL (1536 dimensions).
     * This field is populated by an external ingestion pipeline.
     */
    @Column(name = "embedding_vector", columnDefinition = "vector(1536)")
    private String embeddingVector;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}