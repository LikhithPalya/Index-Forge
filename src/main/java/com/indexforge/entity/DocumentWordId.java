package com.indexforge.entity;

import lombok.*;

import java.io.Serializable;

/**
 * Composite primary key for the DocumentWord join entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DocumentWordId implements Serializable {

    private Long document;
    private Long word;
}