-- =============================================
-- IndexForge - Initial Schema
-- Uses BIGSERIAL for auto-incrementing IDs
-- =============================================

-- Search Documents table
CREATE TABLE search_documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_search_documents_created_at ON search_documents(created_at);
CREATE INDEX idx_search_documents_title ON search_documents(title);

-- Words table (inverted index vocabulary)
CREATE TABLE words (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_words_word ON words(word);

-- Document-Word join table (inverted index postings)
CREATE TABLE document_words (
    document_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    frequency INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (document_id, word_id),
    CONSTRAINT fk_document_words_document
        FOREIGN KEY (document_id) REFERENCES search_documents(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_words_word
        FOREIGN KEY (word_id) REFERENCES words(id) ON DELETE CASCADE
);

CREATE INDEX idx_document_words_document_id ON document_words(document_id);
CREATE INDEX idx_document_words_word_id ON document_words(word_id);
CREATE INDEX idx_document_words_frequency ON document_words(frequency DESC);