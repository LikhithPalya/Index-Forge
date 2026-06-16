package com.indexforge.search.search;

import com.indexforge.search.dto.SearchResultResponse;
import com.indexforge.search.repository.AnimalRepository;
import com.indexforge.search.repository.AnimalSearchProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for performing semantic similarity search over animal documents.
 *
 * Flow:
 *   1. Accept a natural-language query and topK parameter
 *   2. Generate a 384-dim query embedding via QueryEmbeddingService
 *   3. Convert embedding to pgvector-compatible string format
 *   4. Execute pgvector cosine similarity search against PostgreSQL
 *   5. Map projection results to SearchResultResponse DTOs
 *   6. Return ranked results
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SemanticSearchService {

    private final AnimalRepository animalRepository;
    private final QueryEmbeddingService queryEmbeddingService;

    @Value("${search.default-top-k:10}")
    private int defaultTopK;

    @Value("${search.max-top-k:50}")
    private int maxTopK;

    /**
     * Performs semantic search for animals matching the given query.
     *
     * @param query the natural-language search query
     * @param topK number of results to return (capped at max-top-k)
     * @return list of search results ranked by cosine similarity score (descending)
     */
    @Transactional(readOnly = true)
    public List<SearchResultResponse> search(String query, int topK) {
        log.info("Semantic search | query='{}' | topK={}", query, topK);

        // Validate and cap topK
        int effectiveTopK = Math.min(Math.max(topK, 1), maxTopK);

        // Step 1: Generate query embedding (384-dimensional float array)
        float[] embedding = queryEmbeddingService.generateEmbedding(query);
        log.debug("Generated query embedding with {} dimensions", embedding.length);

        // Step 2: Convert float[] to pgvector-compatible string format "[0.1,0.2,...]"
        String vectorString = toPgVectorString(embedding);

        // Step 3: Execute pgvector cosine similarity search
        List<AnimalSearchProjection> results = animalRepository.findBySemanticSimilarity(
                vectorString, effectiveTopK
        );
        log.info("Semantic search returned {} results for query: '{}'", results.size(), query);

        // Step 4: Map projection results to response DTOs
        return results.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Performs semantic search with default topK.
     *
     * @param query the natural-language search query
     * @return list of search results ranked by similarity score
     */
    @Transactional(readOnly = true)
    public List<SearchResultResponse> search(String query) {
        return search(query, defaultTopK);
    }

    /**
     * Converts a float array to pgvector-compatible string format.
     * Example: [0.1, -0.2, 0.3] becomes "[0.1,-0.2,0.3]"
     */
    private String toPgVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(embedding[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private SearchResultResponse mapToResponse(AnimalSearchProjection projection) {
        return SearchResultResponse.builder()
                .animalId(projection.getId())
                .animalName(projection.getName())
                .similarityScore(projection.getSimilarityScore())
                .description(projection.getDescription())
                .wikipediaSummary(projection.getWikipediaSummary())
                .habitat(projection.getHabitat())
                .diet(projection.getDiet())
                .family(projection.getFamily())
                .conservationStatus(projection.getConservationStatus())
                .build();
    }
}