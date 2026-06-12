package com.indexforge.controller;

import com.indexforge.dto.request.CreateDocumentRequest;
import com.indexforge.dto.response.DocumentResponse;
import com.indexforge.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for document operations.
 */
@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Creates a new document.
     *
     * POST /api/v1/documents
     */
    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            @Valid @RequestBody CreateDocumentRequest request) {
        DocumentResponse response = documentService.createDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a document by ID.
     *
     * GET /api/v1/documents/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(@PathVariable Long id) {
        DocumentResponse response = documentService.getDocument(id);
        return ResponseEntity.ok(response);
    }
}