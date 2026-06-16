package com.indexforge.search.repository;

import com.indexforge.search.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for reading animal documents from PostgreSQL.
 * This is a read-only service — data is populated by an external ingestion pipeline.
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    /**
     * Find all animals ordered by name.
     */
    List<Animal> findAllByOrderByNameAsc();

    /**
     * Semantic similarity search using pgvector cosine distance.
     * Uses the IVFFlat index on the embedding column for approximate nearest neighbor search.
     *
     * The query:
     * - Casts the input string to a pgvector vector type
     * - Uses the <=> operator for cosine distance
     * - Computes similarity as 1 - cosine_distance
     * - Orders results by distance (most similar first)
     * - Limits to topK results
     *
     * @param queryVector the query embedding vector as a pgvector-compatible string, e.g. "[0.1,0.2,...]"
     * @param topK maximum number of results to return
     * @return list of projected results ordered by cosine similarity (descending)
     */
    @Query(value = """
            SELECT
                a.id AS id,
                a.name AS name,
                a.description AS description,
                a.wikipedia_summary AS wikipediaSummary,
                a.habitat AS habitat,
                a.diet AS diet,
                a.family AS family,
                a.conservation_status AS conservationStatus,
                1 - (a.embedding <=> CAST(:queryVector AS vector)) AS similarityScore
            FROM animals a
            WHERE a.embedding IS NOT NULL
            ORDER BY a.embedding <=> CAST(:queryVector AS vector)
            LIMIT :topK
            """, nativeQuery = true)
    List<AnimalSearchProjection> findBySemanticSimilarity(
            @Param("queryVector") String queryVector,
            @Param("topK") int topK
    );

    /**
     * Verify that pgvector extension is available.
     * Used for startup health check.
     */
    @Query(value = "SELECT COUNT(*) FROM pg_extension WHERE extname = 'vector'", nativeQuery = true)
    int checkPgvectorExtension();

    /**
     * Count animals that have embeddings populated.
     */
    @Query(value = "SELECT COUNT(*) FROM animals WHERE embedding IS NOT NULL", nativeQuery = true)
    long countWithEmbeddings();
}