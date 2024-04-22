package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.ApplicableLoanProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicableLoanProductRepository extends
        JpaRepository<ApplicableLoanProduct, UUID> {

    ApplicableLoanProduct findByLoanApplicationLoanApplicationIdAndLoanProductLoanProductId(UUID loanApplicationId, UUID loanProductId);

    Page<ApplicableLoanProduct> findByLoanApplicationLoanApplicationId(UUID loanApplicationId, Pageable pageable);

    void deleteByLoanApplicationLoanApplicationIdAndLoanProductLoanProductId(UUID loanApplicationId, UUID loanProductId);

}
