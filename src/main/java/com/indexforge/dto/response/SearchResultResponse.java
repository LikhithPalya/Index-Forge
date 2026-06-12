package com.indexforge.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for search results.
 * Contains a list of matched documents with relevance scores.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultResponse {

    private String query;
    private int totalResults;
    private List<SearchHit> results;

    /**
     * Represents a single search result hit.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchHit {

        private Long id;
        private String title;
        private String snippet;
        private Double score;
        private LocalDateTime createdAt;
    }
}