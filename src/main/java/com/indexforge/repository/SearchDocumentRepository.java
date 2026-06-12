package com.indexforge.repository;

import com.indexforge.entity.SearchDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchDocumentRepository extends JpaRepository<SearchDocument, Long> {
}