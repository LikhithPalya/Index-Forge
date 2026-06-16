package com.indexforge.search.dto;

import lombok.*;

/**
 * Response DTO for a single semantic search result.
 * Contains the matched animal with its similarity score.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultResponse {

    private Long animalId;
    private String animalName;
    private Double similarityScore;
    private String description;
    private String wikipediaSummary;
    private String habitat;
    private String diet;
    private String family;
    private String conservationStatus;
}