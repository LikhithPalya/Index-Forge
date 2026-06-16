package com.indexforge.search.controller;

import com.indexforge.search.dto.SearchResultResponse;
import com.indexforge.search.search.SemanticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for semantic search operations.
 */
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SemanticSearchService semanticSearchService;

    /**
     * Searches animals by natural-language query using semantic similarity.
     *
     * GET /api/v1/search?q=large predators living in asia&topK=5
     *
     * @param q the natural-language search query
     * @param topK number of results to return (optional, default 10, max 50)
     * @return list of search results ranked by similarity score
     */
    @GetMapping
    public ResponseEntity<List<SearchResultResponse>> search(
            @RequestParam("q") String q,
            @RequestParam(value = "topK", required = false, defaultValue = "10") int topK) {
        List<SearchResultResponse> results = semanticSearchService.search(q, topK);
        return ResponseEntity.ok(results);
    }
}