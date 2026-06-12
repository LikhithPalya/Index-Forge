# IndexForge - Architecture Documentation

## Overview

IndexForge is a mini search engine built on an **inverted index** architecture. It allows users to store documents and search them efficiently using keyword-based queries.

## Technology Stack

| Component        | Technology              |
|------------------|-------------------------|
| Language         | Java 21                 |
| Framework        | Spring Boot 3.3         |
| Database         | PostgreSQL 16           |
| ORM              | Spring Data JPA         |
| Migrations       | Flyway                  |
| Build Tool       | Maven                   |
| Containerization | Docker Compose          |

## Project Structure

```
src/main/java/com/indexforge/
├── IndexForgeApplication.java          # Application entry point
├── controller/                         # REST API layer
│   ├── DocumentController.java         # Document CRUD endpoints
│   └── SearchController.java           # Search endpoint
├── dto/                                # Data Transfer Objects
│   ├── request/
│   │   └── CreateDocumentRequest.java  # Document creation payload
│   └── response/
│       ├── DocumentResponse.java       # Document response payload
│       └── SearchResultResponse.java   # Search results with scores
├── entity/                             # JPA entities
│   ├── SearchDocument.java             # Document entity
│   ├── Word.java                       # Word (vocabulary) entity
│   ├── DocumentWord.java               # Join entity (postings list)
│   └── DocumentWordId.java             # Composite key for DocumentWord
├── repository/                         # Data access layer
│   ├── SearchDocumentRepository.java
│   ├── WordRepository.java
│   └── DocumentWordRepository.java
├── service/                            # Business logic layer
│   ├── DocumentService.java            # Document CRUD operations
│   ├── IndexingService.java            # Inverted index construction
│   └── SearchService.java             # Query processing & ranking
├── text/                               # Text processing pipeline
│   └── TextProcessorService.java       # Tokenization, normalization, filtering
└── exception/                          # Error handling
    ├── DocumentNotFoundException.java
    └── GlobalExceptionHandler.java
```

## Data Model

### Entity Relationship Diagram

```
┌──────────────────┐       ┌──────────────────┐       ┌──────────────────┐
│  SearchDocument   │       │   DocumentWord    │       │       Word       │
├──────────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (BIGSERIAL PK)│◄──────│ document_id (FK)  │       │ id (BIGSERIAL PK)│
│ title            │       │ word_id (FK)      │──────►│ word (UNIQUE)    │
│ content          │       │ frequency         │       └──────────────────┘
│ created_at       │       └──────────────────┘
└──────────────────┘
```

### Relationships

- **SearchDocument ↔ Word**: Many-to-Many through DocumentWord
- **DocumentWord**: Join table storing term frequency per document-word pair

## API Endpoints

| Method | Endpoint               | Description                      |
|--------|------------------------|----------------------------------|
| POST   | `/api/v1/documents`    | Create and index a new document  |
| GET    | `/api/v1/documents/{id}` | Retrieve a document by ID      |
| GET    | `/api/v1/search?q=`   | Search documents by keyword      |

## Architecture Layers

```
┌─────────────────────────────────────────────┐
│              Controller Layer                 │
│   (REST endpoints, request validation)       │
├─────────────────────────────────────────────┤
│              Service Layer                    │
│   (Business logic, orchestration)            │
│                                             │
│   ┌─────────────┐  ┌──────────────────┐    │
│   │  Document   │  │    Indexing       │    │
│   │  Service    │  │    Service        │    │
│   └─────────────┘  └──────────────────┘    │
│                                             │
│   ┌─────────────┐  ┌──────────────────┐    │
│   │   Search    │  │  TextProcessor   │    │
│   │   Service   │  │    Service       │    │
│   └─────────────┘  └──────────────────┘    │
├─────────────────────────────────────────────┤
│            Repository Layer                  │
│   (Spring Data JPA, database access)         │
├─────────────────────────────────────────────┤
│              Database Layer                   │
│   (PostgreSQL with Flyway migrations)        │
└─────────────────────────────────────────────┘
```

## Inverted Index Design

The inverted index maps **words** to the **documents** that contain them, along with the **frequency** of occurrence.

### How It Works

1. **Document Ingestion**: A document is saved to the database
2. **Text Processing** (`text/` package):
   - Tokenization: Split content into words
   - Normalization: Lowercase, remove punctuation
   - Stop Word Removal: Filter out common words
3. **Index Construction** (`IndexingService`):
   - For each unique token, find or create a `Word` entry
   - Calculate term frequency
   - Create `DocumentWord` entry linking document → word with frequency
4. **Search** (`SearchService`):
   - Process query through the same text pipeline
   - Look up query terms in the `words` table
   - Find all documents containing those terms via `document_words`
   - Calculate relevance score (e.g., TF-IDF or BM25)
   - Return ranked results

### Scoring (To Be Implemented)

Potential scoring algorithms:
- **Term Frequency (TF)**: Simple word count in document
- **TF-IDF**: Term frequency weighted by inverse document frequency
- **BM25**: Industry-standard probabilistic ranking function

## Text Processing Pipeline (`text/` package)

Current: `TextProcessorService` (skeleton)

Planned expansion:
```
text/
├── TextProcessorService.java   # Orchestrator / facade
├── Tokenizer.java              # Splits text into tokens
├── Normalizer.java             # Lowercasing, accent removal
└── StopWordFilter.java         # Removes common stop words
```

## Running the Application

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven

### Steps

```bash
# 1. Start PostgreSQL
docker-compose up -d

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run
```

### Environment

| Property           | Default Value                              |
|--------------------|--------------------------------------------|
| Server Port        | 8080                                       |
| Database URL       | jdbc:postgresql://localhost:5432/indexforge |
| Database User      | indexforge                                 |
| Database Password  | indexforge                                 |

## Design Principles

1. **Constructor Injection**: All dependencies injected via constructors (Lombok `@RequiredArgsConstructor`)
2. **Clean Architecture**: Clear separation of concerns across layers
3. **DTOs**: API layer decoupled from persistence layer
4. **Immutable IDs**: BIGSERIAL auto-incrementing primary keys
5. **Database-First Migrations**: Schema managed by Flyway
6. **Fail-Fast**: Unimplemented features throw `UnsupportedOperationException`

## TODO - Implementation Roadmap

- [ ] Implement `TextProcessorService.processText()` - tokenization & normalization
- [ ] Implement `TextProcessorService.processQuery()` - query-specific processing
- [ ] Implement `IndexingService.indexDocument()` - build inverted index
- [ ] Implement `IndexingService.reindexAll()` - full re-index capability
- [ ] Implement `SearchService.search()` - query lookup & scoring
- [ ] Add Tokenizer class to `text/` package
- [ ] Add StopWordFilter class to `text/` package
- [ ] Add Normalizer class to `text/` package
- [ ] Implement TF-IDF or BM25 scoring algorithm
- [ ] Add pagination to search results
- [ ] Add integration tests