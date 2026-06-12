package com.indexforge.repository;

import com.indexforge.entity.DocumentWord;
import com.indexforge.entity.DocumentWordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentWordRepository extends JpaRepository<DocumentWord, DocumentWordId> {

    List<DocumentWord> findByDocumentId(Long documentId);

    List<DocumentWord> findByWordId(Long wordId);
}