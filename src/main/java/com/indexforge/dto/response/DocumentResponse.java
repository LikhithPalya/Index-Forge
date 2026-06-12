package com.indexforge.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for returning document details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}