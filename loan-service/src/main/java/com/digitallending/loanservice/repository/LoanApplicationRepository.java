package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {

    Page<LoanApplication> findAllByLoanTypeLoanTypeIdAndLoanApplicationStatusAndUserId(UUID loanTypeId,
                                                                                       LoanApplicationStatus loanApplicationStatus,
                                                                                       UUID userId,Pageable pageable);

    Page<LoanApplication> findAllByLoanTypeLoanTypeIdAndLoanApplicationStatus(UUID loanTypeId,
                                                                              LoanApplicationStatus loanApplicationStatus,
                                                                              Pageable pageable);

    Page<LoanApplication> findAllByLoanTypeLoanTypeIdAndUserId(UUID loanTypeId,
                                                               UUID userId,
                                                               Pageable pageable);

    Page<LoanApplication> findAllByLoanApplicationStatusAndUserId(LoanApplicationStatus loanApplicationStatus,
                                                                  UUID userId,
                                                                  Pageable pageable);

    Page<LoanApplication> findAllByUserId(UUID userId,Pageable pageable);

    Page<LoanApplication> findAllByLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus,Pageable pageable);

    Page<LoanApplication> findAllByLoanTypeLoanTypeId(UUID loanTypeId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE LoanApplication la SET la.loanApplicationStatus = :status WHERE la.loanApplicationId = :loanApplicationId")
    void updateLoanApplicationStatusByLoanApplicationId(
            @Param("loanApplicationId") UUID loanApplicationId, @Param("status") LoanApplicationStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE LoanApplication la SET la.loanApplicationStage = :stage WHERE la.loanApplicationId = :loanApplicationId")
    void updateLoanApplicationStageByLoanApplicationId(
            @Param("loanApplicationId") UUID loanApplicationId, @Param("stage") LoanApplicationStage stage);

    @Modifying
    @Transactional
    @Query("UPDATE LoanApplication la SET la.BREFilterTime = :BREFilterTime WHERE la.loanApplicationId = :loanApplicationId")
    void updateBREFilterTimeByLoanApplicationId(@Param("loanApplicationId") UUID loanApplicationId, @Param("BREFilterTime") Long BREFilterTime);

    Integer countByLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus);

    Integer countByLoanTypeLoanTypeName(LoanTypeName loanTypeName);

    Integer countByAmountBetween(Long minAmount, Long maxAmount);

}
