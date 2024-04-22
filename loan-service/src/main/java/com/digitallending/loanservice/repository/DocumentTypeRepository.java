package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {
    DocumentType findByDocumentTypeName(String documentTypeName);
}
