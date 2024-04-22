package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByLoanApplication_UserId(UUID userId,Pageable pageable);

    Page<Transaction> findByLoanProduct_UserId(UUID userId, Pageable pageable);

    Optional<Transaction> findByLoanApplication_LoanApplicationId(UUID loanApplicationId);

    Page<Transaction> findByLoanProduct_LoanProductId(UUID loanProductId,Pageable pageable);

}
