package com.indexforge.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a unique word in the inverted index vocabulary.
 * Each word maps to one or more documents via the DocumentWord join entity.
 */
@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", nullable = false, unique = true, length = 255)
    private String word;
}