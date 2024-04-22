package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.TransactionValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionValidationRepository extends JpaRepository<TransactionValidation, UUID> {
}
