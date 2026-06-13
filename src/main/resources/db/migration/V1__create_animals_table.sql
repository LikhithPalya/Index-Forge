-- =============================================
-- IndexForge Search - Animal Schema
-- PostgreSQL + pgvector for semantic search
-- =============================================

-- Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- Animals table
CREATE TABLE animals (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    height              VARCHAR(100),
    weight              VARCHAR(100),
    color               VARCHAR(255),
    lifespan            VARCHAR(100),
    diet                VARCHAR(255),
    habitat             TEXT,
    predators           TEXT,
    average_speed       VARCHAR(100),
    countries_found     TEXT,
    description         TEXT,
    wikipedia_summary   TEXT,
    search_document     TEXT,
    embedding_vector    vector(1536),
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

-- B-tree indexes for common lookups
CREATE INDEX idx_animals_name ON animals(name);
CREATE INDEX idx_animals_diet ON animals(diet);
CREATE INDEX idx_animals_habitat ON animals USING gin(to_tsvector('english', habitat));
CREATE INDEX idx_animals_created_at ON animals(created_at);

-- TODO: Add HNSW index for approximate nearest neighbor search once data is loaded
-- Recommended for production with large datasets:
-- CREATE INDEX idx_animals_embedding_hnsw ON animals
--     USING hnsw (embedding_vector vector_cosine_ops)
--     WITH (m = 16, ef_construction = 64);

-- TODO: Alternative IVFFlat index (faster to build, slower to query):
-- CREATE INDEX idx_animals_embedding_ivfflat ON animals
--     USING ivfflat (embedding_vector vector_cosine_ops)
--     WITH (lists = 100);