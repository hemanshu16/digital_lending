package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.BREResultPaginationResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResultResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductRequestDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductResponseDto;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.model.enums.Role;

import java.util.List;
import java.util.UUID;

public interface LoanProductService {
    LoanProduct getProductById(UUID productId);

    LoanProductResponseDto getLoanProductByProductId(UUID productId, UUID userId, Role role);

    LoanProductPaginationResponseDto getFilteredLoanProduct(UUID userId, Role role, UUID lenderId, UUID loanTypeId, int pageNo, int pageSize);

    BREResultPaginationResponseDto showApplicableLoanProductByApplicationId(UUID loanApplicationId, int pageNo, int pageSize);

    UUID getLoanTypeIdByLoanProductId(UUID loanProductId);

    LoanProductResponseDto saveLoanProduct(LoanProductRequestDto loanProductRequestDto);

    Integer getLoanProductCountByLoanType(LoanTypeName loanTypeName);

    Integer getLoanProductCountByAmountRange(Long minAmount, Long maxAmount);

    Long getLoanProductCountByLoanTypeAndUserId(LoanTypeName loanTypeName, UUID userId);
}
