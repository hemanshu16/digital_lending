package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.BREResultPaginationResponseDto;
import com.digitallending.loanservice.model.entity.ApplicableLoanProduct;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;

import java.util.List;
import java.util.UUID;

public interface ApplicableLoanProductService {

    void saveApplicableLoanProduct(LoanProduct loanProduct, LoanApplication loanApplication, Double interest, Integer score);

    ApplicableLoanProduct findInterestRateByLoanApplicationIdAndLoanProductId(UUID loanApplicationId, UUID loanProductId);

    void deleteApplicableLoanProductByLoanApplicationIdAndLoanProductId(UUID loanApplicationId, UUID loanProductId);

    BREResultPaginationResponseDto getAllApplicableLoanProductByLoanApplicationId(UUID loanApplicationId, int pageNo, int pagesize);

}

