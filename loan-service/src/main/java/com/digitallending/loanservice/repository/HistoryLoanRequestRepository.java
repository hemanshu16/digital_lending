package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.dto.loanstatistic.LoanProductCount;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryLoanRequestRepository extends JpaRepository<HistoryLoanRequest, UUID> {

    boolean existsByLoanApplicationLoanApplicationId(UUID loanApplicationId);

    boolean existsByLoanApplicationLoanApplicationIdAndStatus(UUID loanApplicationId, LoanRequestStatus status);

    List<HistoryLoanRequest> findByStatus(LoanRequestStatus loanRequestStatus);

    List<HistoryLoanRequest> findByLoanApplicationLoanApplicationIdAndStatus(
            UUID loanApplicationId, LoanRequestStatus status);

    List<HistoryLoanRequest> findByLoanProductLoanProductIdAndStatus(
            UUID loanProductId, LoanRequestStatus status);

    @Query("SELECT new com.digitallending.loanservice.model.dto.loanstatistic.LoanProductCount(lr.loanProduct.loanProductName,COUNT(lr)) FROM HistoryLoanRequest lr" +
            " WHERE lr.loanProduct.userId = :userId AND lr.status = :loanRequestStatus GROUP BY lr.loanProduct.loanProductName")
    List<LoanProductCount> countLoanRequestsByUserIdIdAndStatus(@Param("userId") UUID userId,
                                                                @Param("loanRequestStatus") LoanRequestStatus loanRequestStatus);

    Long countByLoanProductUserIdAndStatus(UUID userId,LoanRequestStatus loanRequestStatus);
    @Modifying
    @Transactional
    @Query("UPDATE HistoryLoanRequest hlr SET hlr.providerAccountId = :newProviderAccountId WHERE hlr.loanRequestId = :loanRequestId")
    void updateProviderAccountId(UUID loanRequestId, UUID newProviderAccountId);
}
