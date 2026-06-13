# 🔍 IndexForge Search

A semantic search service for the Animal Kingdom, powered by PostgreSQL + pgvector.

IndexForge Search serves natural-language similarity queries over animal documents. It converts user queries into embedding vectors and finds the most relevant animals using cosine similarity search.

---

## 🏗️ Architecture

```
┌─────────────────────────────────┐
│         User Query              │
│  "fast predators in Africa"     │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│      Query Embedding            │
│  Convert text → vector (1536d)  │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│   Vector Similarity Search      │
│  pgvector cosine distance (<=>)│
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│        Rank Results             │
│  Order by similarity score      │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│   Return Animal Documents       │
│  { name, description, score }   │
└─────────────────────────────────┘
```

## 📋 What This Service Does

- ✅ Serves semantic search requests over animal documents
- ✅ Reads animal data from PostgreSQL (pgvector)
- ✅ Returns ranked results with similarity scores
- ✅ Provides animal CRUD read endpoints

## 🚫 What This Service Does NOT Do

- ❌ Read CSV files or ingest data
- ❌ Call Wikipedia or any external data source
- ❌ Perform data ingestion or ETL
- ❌ Generate embeddings for documents (handled externally)

**PostgreSQL is the only source of truth.**

---

## 🛠️ Tech Stack

| Component    | Technology              |
|--------------|-------------------------|
| Language     | Java 21                 |
| Framework    | Spring Boot 3.3         |
| Database     | PostgreSQL 16           |
| Vector Store | pgvector                |
| Migrations   | Flyway                  |
| Build Tool   | Maven                   |
| Code Gen     | Lombok                  |
| Container    | Docker Compose          |

---

## 📁 Project Structure

```
src/main/java/com/indexforge/search/
├── IndexForgeSearchApplication.java       # Entry point
├── controller/
│   ├── AnimalController.java              # GET /api/v1/animals, GET /api/v1/animals/{id}
│   └── SearchController.java             # GET /api/v1/search?q=
├── dto/
│   ├── AnimalResponse.java               # Full animal details
│   └── SearchResultResponse.java         # Search hit (id, name, description, score)
├── entity/
│   └── Animal.java                        # JPA entity with pgvector column
├── repository/
│   └── AnimalRepository.java             # JPA + native pgvector queries
├── service/
│   └── AnimalService.java                # Read-only animal operations
├── search/
│   ├── SemanticSearchService.java        # Orchestrates vector search
│   └── QueryEmbeddingService.java        # Converts query → embedding vector
├── config/
│   └── AppConfig.java                    # Spring beans configuration
└── exception/
    ├── AnimalNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🗄️ Database Schema

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

| Name | Type | Column | Purpose |
|------|------|--------|---------|
| `idx_animals_name` | B-tree | `name` | Fast name lookups |
| `idx_animals_diet` | B-tree | `diet` | Filter by diet |
| `idx_animals_habitat` | GIN | `habitat` | Full-text search |
| `idx_animals_created_at` | B-tree | `created_at` | Time-based sorting |
| *(future)* | HNSW | `embedding_vector` | ANN vector search |

---

## 🌐 API Endpoints

### List All Animals

```
GET /api/v1/animals
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Cheetah",
    "height": "70-90 cm",
    "weight": "21-72 kg",
    "color": "Tan with black spots",
    "lifespan": "10-12 years",
    "diet": "Carnivore",
    "habitat": "African savanna",
    "predators": "Lions, Hyenas",
    "averageSpeed": "112 km/h",
    "countriesFound": "Kenya, Tanzania, Namibia",
    "description": "The cheetah is the fastest land animal...",
    "wikipediaSummary": "...",
    "createdAt": "2026-06-13T12:00:00"
  }
]
```

### Get Animal by ID

```
GET /api/v1/animals/{id}
```

**Response:** `200 OK` — Same format as above (single object)

**Error:** `404 Not Found`
```json
{
  "timestamp": "2026-06-13T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Animal not found with id: 99"
}
```

### Semantic Search

```
GET /api/v1/search?q=fast animals that hunt in packs
```

**Response:** `200 OK`
```json
[
  {
    "animalId": 42,
    "animalName": "African Wild Dog",
    "description": "The African wild dog is a highly social predator...",
    "similarityScore": 0.91
  },
  {
    "animalId": 7,
    "animalName": "Cheetah",
    "description": "The cheetah is the fastest land animal...",
    "similarityScore": 0.87
  }
]
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- Docker & Docker Compose
- Maven

### Run

```bash
# 1. Start PostgreSQL with pgvector
docker-compose up -d

# 2. Build the project
mvn clean install -DskipTests

# 3. Run the application
mvn spring-boot:run
```

The service starts on `http://localhost:8080`.

### Verify

```bash
# Health check
curl http://localhost:8080/api/v1/animals

# Search (returns empty until implementation is complete)
curl "http://localhost:8080/api/v1/search?q=large%20herbivores"
```

---

## 🧩 Implementation Status

| Component | Status |
|-----------|--------|
| Animal entity + migration | ✅ Done |
| Animal read endpoints | ✅ Done |
| Search endpoint (skeleton) | ✅ Done |
| QueryEmbeddingService | 🔲 TODO |
| SemanticSearchService logic | 🔲 TODO |
| HNSW vector index | 🔲 TODO |
| Result pagination | 🔲 TODO |
| Similarity threshold | 🔲 TODO |

---

## 📐 Design Principles

1. **Read-only** — This service only reads from PostgreSQL. Data population happens externally.
2. **Constructor injection** — All dependencies injected via `@RequiredArgsConstructor`.
3. **Clean layering** — Controller → Service → Repository. No business logic in controllers.
4. **DTO separation** — API responses decoupled from JPA entities.
5. **Fail-fast** — Unimplemented features throw `UnsupportedOperationException` with clear messages.
6. **Database-first** — Schema managed by Flyway migrations.

---

## 📄 License

MIT