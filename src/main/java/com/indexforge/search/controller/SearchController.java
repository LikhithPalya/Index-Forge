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
     * GET /api/v1/search?q=fast animals that live in Africa
     *
     * @param q the natural-language search query
     * @return list of search results ranked by similarity score
     */
    @GetMapping
    public ResponseEntity<List<SearchResultResponse>> search(@RequestParam("q") String q) {
        List<SearchResultResponse> results = semanticSearchService.search(q);
        return ResponseEntity.ok(results);
    }
}