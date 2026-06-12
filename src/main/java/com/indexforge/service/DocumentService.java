package com.indexforge.service;

import com.indexforge.dto.request.CreateDocumentRequest;
import com.indexforge.dto.response.DocumentResponse;
import com.indexforge.entity.SearchDocument;
import com.indexforge.exception.DocumentNotFoundException;
import com.indexforge.repository.SearchDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for document CRUD operations.
 * Delegates indexing to IndexingService after document creation.
 */
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final SearchDocumentRepository searchDocumentRepository;
    private final IndexingService indexingService;

    /**
     * Creates a new document and triggers indexing.
     *
     * @param request the document creation request
     * @return the created document response
     */
    @Transactional
    public DocumentResponse createDocument(CreateDocumentRequest request) {
        SearchDocument document = SearchDocument.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        SearchDocument saved = searchDocumentRepository.save(document);

        // TODO: Trigger indexing after document is saved
        // indexingService.indexDocument(saved);

        return mapToResponse(saved);
    }

    /**
     * Retrieves a document by its ID.
     *
     * @param id the document ID
     * @return the document response
     * @throws DocumentNotFoundException if document is not found
     */
    @Transactional(readOnly = true)
    public DocumentResponse getDocument(Long id) {
        SearchDocument document = searchDocumentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));

        return mapToResponse(document);
    }

    private DocumentResponse mapToResponse(SearchDocument document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .createdAt(document.getCreatedAt())
                .build();
    }
}