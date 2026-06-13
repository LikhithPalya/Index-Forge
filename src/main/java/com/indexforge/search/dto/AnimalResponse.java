package com.indexforge.search.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for returning animal document details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalResponse {

    private Long id;
    private String name;
    private String height;
    private String weight;
    private String color;
    private String lifespan;
    private String diet;
    private String habitat;
    private String predators;
    private String averageSpeed;
    private String countriesFound;
    private String description;
    private String wikipediaSummary;
    private LocalDateTime createdAt;
}