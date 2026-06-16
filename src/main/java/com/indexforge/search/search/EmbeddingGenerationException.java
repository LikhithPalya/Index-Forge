package com.indexforge.search.search;

/**
 * Exception thrown when embedding generation fails.
 */
public class EmbeddingGenerationException extends RuntimeException {

    public EmbeddingGenerationException(String message) {
        super(message);
    }

    public EmbeddingGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}