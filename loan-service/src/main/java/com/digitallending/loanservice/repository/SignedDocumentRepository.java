package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SignedDocumentRepository extends JpaRepository<SignedDocument, UUID> {

    Page<SignedDocument> findAllByStatus(SignedDocumentStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE SignedDocument sd SET sd.status = :status WHERE sd.signedDocumentId = :signedDocumentId")
    void changeSignedDocumentStatusById(@Param("signedDocumentId") UUID signedDocumentId,
                                        @Param("status")SignedDocumentStatus status);
}
