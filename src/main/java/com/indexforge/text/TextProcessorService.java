package com.indexforge.text;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for text processing operations.
 *
 * This is the entry point for all text transformations needed by
 * the indexing and search pipelines.
 *
 * Future components in this package:
 * - Tokenizer: splits text into individual tokens
 * - Normalizer: lowercases, removes accents, etc.
 * - StopWordFilter: removes common stop words (the, is, at, etc.)
 *
 * TODO: Implement text processing pipeline:
 *   1. Tokenize text into words (split on whitespace/punctuation)
 *   2. Normalize tokens (lowercase, trim)
 *   3. Remove stop words
 *   4. Optionally apply stemming/lemmatization
 */
@Service
@RequiredArgsConstructor
public class TextProcessorService {

    /**
     * Processes raw text and returns a list of normalized, filtered tokens.
     *
     * TODO: Implement this method
     * Steps:
     *   1. Tokenize: split text into individual words
     *   2. Normalize: convert to lowercase, remove punctuation
     *   3. Filter: remove stop words and tokens shorter than minimum length
     *   4. (Optional) Stem: reduce words to their root form
     *
     * @param text the raw text to process
     * @return list of processed tokens
     */
    public List<String> processText(String text) {
        // TODO: Implement text processing pipeline
        throw new UnsupportedOperationException("Text processing not yet implemented");
    }

    /**
     * Processes a search query. May apply different rules than document processing
     * (e.g., no stemming, different stop word handling).
     *
     * TODO: Implement this method
     *
     * @param query the search query
     * @return list of processed query tokens
     */
    public List<String> processQuery(String query) {
        // TODO: Implement query processing (may differ from document processing)
        throw new UnsupportedOperationException("Query processing not yet implemented");
    }
}