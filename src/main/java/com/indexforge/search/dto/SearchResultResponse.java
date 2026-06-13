package com.indexforge.search.dto;

import lombok.*;

/**
 * Response DTO for a single semantic search result.
 * Contains the matched animal and its similarity score.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultResponse {

    private Long animalId;
    private String animalName;
    private String description;
    private Double similarityScore;
}