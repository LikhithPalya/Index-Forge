# IndexForge Search - Architecture

## Purpose

Serve semantic search requests over animal documents stored in PostgreSQL + pgvector.

This service does **NOT**:
- Read CSV files or ingest data
- Call Wikipedia or external data sources
- Implement RAG, chat, or hybrid search
- Generate document embeddings

**PostgreSQL is the only source of truth.**

## V1 Semantic Search Pipeline

```
User Query ("large predators living in Asia")
    │
    ▼
┌─────────────────────────────────────┐
│       Query Embedding Service        │
│  Convert query text → float[384]     │
│  (all-MiniLM-L6-v2 compatible)      │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│    pgvector Similarity Search        │
│  embedding <=> query_vector          │
│  (cosine distance, IVFFlat index)    │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│        Rank Results                  │
│  similarity_score = 1 - distance     │
│  ORDER BY distance ASC, LIMIT topK   │
└──────────────────┬──────────────────┘
                   │
                   ▼
┌─────────────────────────────────────┐
│     JSON Response                    │
│  [{ animalId, animalName,            │
│     similarityScore, description }]  │
└─────────────────────────────────────┘
```

## Technology Stack

| Component    | Technology                           |
|--------------|--------------------------------------|
| Language     | Java 21                              |
| Framework    | Spring Boot 3.3                      |
| Database     | PostgreSQL 16                        |
| Vector Store | pgvector (IVFFlat index)             |
| Embeddings   | 384-dim (all-MiniLM-L6-v2)          |
| Build Tool   | Maven                                |
| Code Gen     | Lombok                               |

## Database

- **196 animal documents** pre-loaded
- **pgvector extension** enabled
- **embedding column**: `vector(384)`
- **IVFFlat index** on embedding column for ANN search
- **Wikipedia summaries** populated for all animals

## Package Structure

```
com.indexforge.search
├── IndexForgeSearchApplication.java
├── config/
│   ├── AppConfig.java                     # Spring beans (future: WebClient for embedding API)
│   └── DatabaseHealthValidator.java       # Startup checks: DB + pgvector + embeddings
├── controller/
│   ├── AnimalController.java              # GET /api/v1/animals, GET /api/v1/animals/{id}
│   └── SearchController.java             # GET /api/v1/search?q=&topK=
├── dto/
│   ├── AnimalResponse.java               # Full animal details
│   └── SearchResultResponse.java         # Search hit (id, name, score, description, ...)
├── entity/
│   └── Animal.java                        # JPA entity mapping animals table
├── exception/
│   ├── AnimalNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── AnimalRepository.java             # JPA + native pgvector similarity query
│   └── AnimalSearchProjection.java       # Interface projection for search results
├── search/
│   ├── QueryEmbeddingService.java        # Interface: query → float[384]
│   ├── PlaceholderQueryEmbeddingService.java  # TODO: Replace with real provider
│   ├── SemanticSearchService.java        # Orchestrates: embed → search → map
│   └── EmbeddingGenerationException.java # Custom exception
└── service/
    └── AnimalService.java                # Read-only CRUD for animals
```

## API Endpoints

| Method | Endpoint               | Parameters              | Description                  |
|--------|------------------------|-------------------------|------------------------------|
| GET    | `/api/v1/animals`      | —                       | List all animals             |
| GET    | `/api/v1/animals/{id}` | —                       | Get animal by ID             |
| GET    | `/api/v1/search`       | `q` (required), `topK` (optional, default=10) | Semantic search |

### Search Response

```json
[
  {
    "animalId": 101,
    "animalName": "Tiger",
    "similarityScore": 0.94,
    "description": "The tiger is the largest living cat species...",
    "wikipediaSummary": "...",
    "habitat": "Tropical forests, grasslands",
    "diet": "Carnivore",
    "family": "Felidae",
    "conservationStatus": "Endangered"
  }
]
```

## Implementation Status

| Component | Status | Notes |
|-----------|--------|-------|
| Animal entity | ✅ Done | Maps to existing animals table |
| AnimalRepository | ✅ Done | Native pgvector similarity query with projection |
| SemanticSearchService | ✅ Done | Full flow: embed → search → map |
| SearchController | ✅ Done | GET /api/v1/search?q=&topK= |
| Database startup validation | ✅ Done | Checks DB, pgvector, embeddings |
| QueryEmbeddingService interface | ✅ Done | Clean interface returning float[384] |
| QueryEmbeddingService implementation | 🔲 TODO | Placeholder throws exception |

## TODO: Connect Embedding Provider

The `PlaceholderQueryEmbeddingService` must be replaced with a real implementation.

**Location**: `com.indexforge.search.search.PlaceholderQueryEmbeddingService`

**Requirements**:
- Must produce 384-dimensional vectors
- Must be compatible with all-MiniLM-L6-v2 model
- Must implement `QueryEmbeddingService` interface

**Options**:
1. Python microservice running sentence-transformers (recommended)
2. HuggingFace Inference API
3. ONNX Runtime with exported model
4. OpenAI with matching dimension configuration

## Running

```bash
# Start the application (PostgreSQL must be running with data)
mvn spring-boot:run

# Test search endpoint
curl "http://localhost:8080/api/v1/search?q=large%20predators%20in%20asia&topK=5"