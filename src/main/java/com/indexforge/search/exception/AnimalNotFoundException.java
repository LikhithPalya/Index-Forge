package com.indexforge.search.exception;

/**
 * Exception thrown when an animal is not found by its ID.
 */
public class AnimalNotFoundException extends RuntimeException {

    public AnimalNotFoundException(Long id) {
        super("Animal not found with id: " + id);
    }
}