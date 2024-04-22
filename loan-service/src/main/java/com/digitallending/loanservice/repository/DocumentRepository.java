package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findAllByLoanApplicationLoanApplicationId(UUID loanApplicationId);

    Optional<Document> findByLoanApplicationLoanApplicationIdAndDocumentTypeDocumentTypeId(UUID loanApplicationId, UUID documentTypeId);

    Long countByLoanApplicationLoanApplicationId(UUID loanApplicationId);
}
