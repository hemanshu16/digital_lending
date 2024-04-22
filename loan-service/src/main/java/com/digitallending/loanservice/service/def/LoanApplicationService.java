package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationStatusRequestDto;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.model.enums.Role;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationService {
    LoanApplication getLoanApplicationById(UUID loanApplicationId);

    LoanApplicationResponseDto getLoanApplicationByLoanApplicationId(UUID loanApplicationId, Role role, UUID userId);

    LoanApplicationResponseDto getLoanApplicationByLoanRequestId(UUID loanRequestId, UUID userId, Role role);

    LoanApplicationPaginationResponseDto getFilteredLoanApplication(
            UUID userId, Role role, UUID msmeUserId, LoanApplicationStatus status,UUID loanTypeId, int pageNo, int pageSize);

    String updateLoanApplicationStatus(LoanApplicationStatusRequestDto loanApplicationStatusRequestDto);

    LoanApplicationResponseDto saveLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto, UUID userId);

    void updateLoanApplicationBREFilterTime(UUID loanApplicationId, Long BREFilterTime);

    void saveSignedDocument(LoanApplication loanApplication, SignedDocument signedDocument);

    Integer getLoanApplicationCountByLoanType(LoanTypeName loanTypeName);

    Integer getLoanApplicationByAmountRange(Long minAmount, Long maxAmount);

    Integer getLoanApplicationCountByStatus(LoanApplicationStatus status);

    String submitLoanApplication(UUID loanApplicationId, UUID userId);

    void updateLoanApplicationStage(UUID loanApplicationId, LoanApplicationStage documentRemaining);

    byte[] getSignDocumentContentByLoanApplicationId(UUID loanApplicationId, UUID userId);
}
