package com.indexforge.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Join entity representing the relationship between a SearchDocument and a Word.
 * Stores the frequency of a word's occurrence within a specific document.
 * This forms the core of the inverted index postings list.
 */
@Entity
@Table(name = "document_words")
@IdClass(DocumentWordId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentWord {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private SearchDocument document;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;
}