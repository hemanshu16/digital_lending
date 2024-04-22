package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, UUID> {

    List<LoanRequest> findByLoanApplicationLoanApplicationId(UUID loanApplicationId);

    @Query(value = "SELECT NEW com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto( lr.loanRequestId AS loanRequestId, " +
            "lr.loanApplication.loanApplicationId AS loanApplicationId, " +
            "lr.loanProduct.loanProductId AS loanProductId, " +
            "lr.interestRate AS interestRate, " +
            "lr.status AS status, " +
            "lr.providerAccountId AS providerAccountId, " +
            "lr.receiverAccountId AS receiverAccountId, " +
            "lr.score AS score) " +
            "FROM LoanRequest lr " +
            "WHERE lr.loanProduct.loanProductId = :productId AND lr.status = :status " +
            "UNION " +
            "SELECT NEW com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto( hlr.loanRequestId AS loanRequestId, " +
            "hlr.loanApplication.loanApplicationId AS loanApplicationId, " +
            "hlr.loanProduct.loanProductId AS loanProductId, " +
            "hlr.interestRate AS interestRate, " +
            "hlr.status AS status, " +
            "hlr.providerAccountId AS providerAccountId, " +
            "hlr.receiverAccountId AS receiverAccountId, " +
            "hlr.score AS score) " +
            "FROM HistoryLoanRequest hlr " +
            "WHERE hlr.loanProduct.loanProductId = :productId AND hlr.status = :status",
            countQuery = "SELECT COUNT(*) FROM (SELECT lr.loanRequestId AS loanRequestId FROM LoanRequest lr " +
                    "WHERE lr.loanProduct.loanProductId = :productId AND lr.status = :status " +
                    "UNION ALL " +
                    "SELECT hlr.loanRequestId AS loanRequestId FROM HistoryLoanRequest hlr WHERE hlr.loanProduct.loanProductId = :productId AND hlr.status = :status) as total"
    )
    Page<LoanRequestResponseDto> findAllByLoanProductLoanProductIdAndStatus(@Param("productId") UUID productId, @Param("status") LoanRequestStatus status, Pageable pageable);

    List<LoanRequest> findByLoanApplicationLoanApplicationIdAndStatus(UUID loanApplicationId, LoanRequestStatus status);

    Long countByLoanProductUserIdAndStatus(UUID userId, LoanRequestStatus loanRequestStatus);

    boolean existsByLoanApplicationLoanApplicationIdAndStatusIn(
            UUID loanApplicationId,
            List<LoanRequestStatus> loanRequestStatus);
}

