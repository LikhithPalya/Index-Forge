package com.indexforge.controller;

import com.indexforge.dto.response.SearchResultResponse;
import com.indexforge.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for search operations.
 */
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * Searches documents by query.
     *
     * GET /api/v1/search?q=term
     *
     * @param q the search query
     * @return search results with relevance scores
     */
    @GetMapping
    public ResponseEntity<SearchResultResponse> search(@RequestParam("q") String q) {
        SearchResultResponse response = searchService.search(q);
        return ResponseEntity.ok(response);
    }
}