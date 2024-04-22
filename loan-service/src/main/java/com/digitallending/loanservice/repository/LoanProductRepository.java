package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface LoanProductRepository extends JpaRepository<LoanProduct, UUID> {
    Page<LoanProduct> findAllByUserId(UUID userId, Pageable pageable);

    Page<LoanProduct> findAllByUserIdAndLoanTypeLoanTypeId(UUID userId, UUID loanTypeId, Pageable pageable);

    Page<LoanProduct> findAllByLoanTypeLoanTypeId(UUID loanTypeId,Pageable pageable);

    List<LoanProduct> findByLoanTypeLoanTypeId(UUID loanTypeId);

    Optional<LoanProduct> findByLoanProductId(UUID loanProductId);

    List<LoanProduct> findByCreationTimeGreaterThanEqualAndLoanTypeLoanTypeId(Long creationTime, UUID loanTypeId);

    Integer countByLoanTypeLoanTypeName(LoanTypeName loanTypeName);

    Integer countByMaxAmountBetween(Long minAmount, Long maxAmount);

    Long countByLoanTypeLoanTypeNameAndUserId(LoanTypeName loanTypeName,UUID userId);
}


