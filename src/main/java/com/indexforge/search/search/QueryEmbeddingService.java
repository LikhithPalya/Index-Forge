package com.indexforge.search.search;

/**
 * Service interface for generating embedding vectors from search queries.
 *
 * Implementations of this interface will connect to an external embedding provider
 * (e.g., OpenAI, HuggingFace Inference API, local sentence-transformers model)
 * to convert natural-language queries into dense vector representations.
 *
 * The embedding model must produce vectors of dimension 384 to match the
 * all-MiniLM-L6-v2 embeddings stored in the database.
 */
public interface QueryEmbeddingService {

    /**
     * Generates a 384-dimensional embedding vector for the given search query.
     *
     * @param query the natural-language search query (e.g., "large predators in Asia")
     * @return float array of dimension 384 representing the query embedding
     * @throws EmbeddingGenerationException if embedding generation fails
     */
    float[] generateEmbedding(String query);
}