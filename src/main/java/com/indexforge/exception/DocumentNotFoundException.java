package com.indexforge.exception;

/**
 * Exception thrown when a document is not found by its ID.
 */
public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(Long id) {
        super("Document not found with id: " + id);
    }
}