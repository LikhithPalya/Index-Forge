# IndexForge Search - Architecture

## Purpose

Serve semantic search requests over animal documents stored in PostgreSQL + pgvector.

This service does **NOT**:
- Read CSV files
- Call Wikipedia
- Perform data ingestion

Its only source of truth is **PostgreSQL**.

## Technology Stack

| Component    | Technology         |
|--------------|--------------------|
| Language     | Java 21            |
| Framework    | Spring Boot 3.3    |
| Database     | PostgreSQL 16      |
| Vector Store | pgvector           |
| Migrations   | Flyway             |
| Build Tool   | Maven              |
| Code Gen     | Lombok             |

## Search Pipeline

```
User Query
    │
    ▼
Query Embedding
    │
    ▼
Vector Similarity Search (pgvector cosine distance)
    │
    ▼
Rank Results
    │
    ▼
Return Animal Documents
```

## Package Structure

```
com.indexforge.search
├── IndexForgeSearchApplication.java
├── controller/
│   ├── AnimalController.java
│   └── SearchController.java
├── dto/
│   ├── AnimalResponse.java
│   └── SearchResultResponse.java
├── entity/
│   └── Animal.java
├── repository/
│   └── AnimalRepository.java
├── service/
│   └── AnimalService.java
├── search/
│   ├── SemanticSearchService.java
│   └── QueryEmbeddingService.java
├── config/
│   └── AppConfig.java
└── exception/
    ├── AnimalNotFoundException.java
    └── GlobalExceptionHandler.java
```

## API Endpoints

| Method | Endpoint               | Description                         |
|--------|------------------------|-------------------------------------|
| GET    | `/api/v1/animals`      | List all animals                    |
| GET    | `/api/v1/animals/{id}` | Get animal by ID                    |
| GET    | `/api/v1/search?q=`   | Semantic search by natural language |

### Search Response

```json
[
  {
    "animalId": 42,
    "animalName": "Cheetah",
    "description": "The Cheetah is the fastest land animal...",
    "similarityScore": 0.94
  }
]
```

## Database Schema

```sql
CREATE TABLE animals (
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    height            VARCHAR(100),
    weight            VARCHAR(100),
    color             VARCHAR(255),
    lifespan          VARCHAR(100),
    diet              VARCHAR(255),
    habitat           TEXT,
    predators         TEXT,
    average_speed     VARCHAR(100),
    countries_found   TEXT,
    description       TEXT,
    wikipedia_summary TEXT,
    search_document   TEXT,
    embedding_vector  vector(1536),
    created_at        TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### Indexes

| Index | Type | Column |
|-------|------|--------|
| idx_animals_name | B-tree | name |
| idx_animals_diet | B-tree | diet |
| idx_animals_habitat | GIN (full-text) | habitat |
| idx_animals_created_at | B-tree | created_at |
| (future) HNSW | vector_cosine_ops | embedding_vector |

## Design Principles

1. **Read-only service** — data is populated externally
2. **Constructor injection** — via Lombok `@RequiredArgsConstructor`
3. **Clean separation** — Controller > Service > Repository
4. **DTOs** — API decoupled from entities
5. **Fail-fast** — unimplemented features throw `UnsupportedOperationException`
6. **Database-first** — schema managed by Flyway

## Running

```bash
docker-compose up -d        # Start PostgreSQL with pgvector
mvn clean install -DskipTests
mvn spring-boot:run         # Start on port 8080
```

## Future Implementation (TODO)

1. `QueryEmbeddingService.embed()` — convert query text to vector
2. `SemanticSearchService.search()` — vector similarity search + ranking
3. HNSW/IVFFlat index on `embedding_vector` column
4. Minimum similarity threshold filtering
5. Result pagination