package com.indexforge.search.repository;

/**
 * Projection interface for semantic search results from native pgvector queries.
 * Maps the columns returned by the similarity search query to typed accessors.
 */
public interface AnimalSearchProjection {

    Long getId();

    String getName();

    String getDescription();

    String getWikipediaSummary();

    String getHabitat();

    String getDiet();

    String getFamily();

    String getConservationStatus();

    Double getSimilarityScore();
}