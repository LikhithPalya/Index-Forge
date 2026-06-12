package com.indexforge.service;

import com.indexforge.dto.response.SearchResultResponse;
import com.indexforge.repository.DocumentWordRepository;
import com.indexforge.repository.SearchDocumentRepository;
import com.indexforge.repository.WordRepository;
import com.indexforge.text.TextProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Service responsible for searching documents using the inverted index.
 *
 * This service will:
 * 1. Process the search query using TextProcessorService
 * 2. Look up query terms in the inverted index
 * 3. Calculate relevance scores (e.g., TF-IDF)
 * 4. Return ranked results
 *
 * TODO: Implement the search logic:
 *   - Tokenize and normalize the query using TextProcessorService
 *   - For each query term, retrieve matching documents from the inverted index
 *   - Calculate relevance scores (consider TF-IDF or BM25)
 *   - Rank and return results
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    private final WordRepository wordRepository;
    private final DocumentWordRepository documentWordRepository;
    private final SearchDocumentRepository searchDocumentRepository;
    private final TextProcessorService textProcessorService;

    /**
     * Searches for documents matching the given query.
     *
     * TODO: Implement this method
     * Steps:
     *   1. Process query through TextProcessorService
     *   2. Look up each query term in the Word table
     *   3. Retrieve DocumentWord entries for matching words
     *   4. Calculate relevance scores (TF-IDF, BM25, or simple frequency)
     *   5. Aggregate scores per document
     *   6. Sort by score descending
     *   7. Build and return SearchResultResponse
     *
     * @param query the search query string
     * @return search results with scores
     */
    @Transactional(readOnly = true)
    public SearchResultResponse search(String query) {
        // TODO: Implement search logic
        return SearchResultResponse.builder()
                .query(query)
                .totalResults(0)
                .results(Collections.emptyList())
                .build();
    }
}