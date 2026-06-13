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

    // TODO: Implement vector similarity search
    // This method will perform cosine similarity search using pgvector.
    // It will:
    //   1. Accept a query embedding vector (as String in pgvector format)
    //   2. Use the <=> operator for cosine distance
    //   3. Return animals ranked by similarity
    //   4. Include the similarity score in the result

    /**
     * Semantic similarity search using pgvector cosine distance.
     *
     * TODO: Uncomment and use once QueryEmbeddingService is implemented.
     *
     * @param queryEmbedding the query embedding vector as a pgvector-compatible string
     * @param limit maximum number of results
     * @return list of [Animal fields..., similarity_score] ordered by similarity
     */
    @Query(value = """
            SELECT a.id, a.name, a.description, 
                   1 - (a.embedding_vector <=> CAST(:queryEmbedding AS vector)) AS similarity_score
            FROM animals a
            WHERE a.embedding_vector IS NOT NULL
            ORDER BY a.embedding_vector <=> CAST(:queryEmbedding AS vector)
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findBySemanticSimilarity(
            @Param("queryEmbedding") String queryEmbedding,
            @Param("limit") int limit
    );
}