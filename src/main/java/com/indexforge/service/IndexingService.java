package com.indexforge.service;

import com.indexforge.entity.SearchDocument;
import com.indexforge.repository.DocumentWordRepository;
import com.indexforge.repository.WordRepository;
import com.indexforge.text.TextProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service responsible for building and maintaining the inverted index.
 *
 * This service will:
 * 1. Accept a document
 * 2. Use TextProcessorService to tokenize and normalize the content
 * 3. Calculate word frequencies
 * 4. Persist Word entities and DocumentWord relationships
 *
 * TODO: Implement the indexing logic:
 *   - Tokenize document content using TextProcessorService
 *   - For each unique token, find or create the Word entity
 *   - Calculate term frequency for each word in the document
 *   - Create DocumentWord entries linking document to words with frequencies
 */
@Service
@RequiredArgsConstructor
public class IndexingService {

    private final WordRepository wordRepository;
    private final DocumentWordRepository documentWordRepository;
    private final TextProcessorService textProcessorService;

    /**
     * Indexes a document by extracting words and storing their frequencies.
     *
     * TODO: Implement this method
     * Steps:
     *   1. Extract text from document (title + content)
     *   2. Process text through TextProcessorService (tokenize, normalize, filter)
     *   3. Calculate term frequencies
     *   4. Persist words and document-word relationships
     *
     * @param document the document to index
     */
    public void indexDocument(SearchDocument document) {
        // TODO: Implement indexing logic
        throw new UnsupportedOperationException("Indexing logic not yet implemented");
    }

    /**
     * Re-indexes all documents. Useful for rebuilding the entire index.
     *
     * TODO: Implement this method
     */
    public void reindexAll() {
        // TODO: Implement full re-indexing logic
        throw new UnsupportedOperationException("Re-indexing logic not yet implemented");
    }
}