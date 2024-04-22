package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestAcceptRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestOfferDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanProductDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestStatusDto;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;

import java.util.List;
import java.util.UUID;

public interface LoanRequestService {
    LoanRequest getLoanRequestById(UUID loanRequestId);

    LoanRequestResponseDto getLoanRequestResponseDtoById(UUID loanRequestId);

    List<LoanRequestResponseWithLoanProductDto> getAllLoanRequestByLoanApplicationIdAndStatus(
            UUID loanApplicationId, String role, UUID userId, LoanRequestStatus status);

    LoanRequestPaginationResponseDto getAllLoanRequestByLoanProductIdAndStatus(
            UUID loanProductId, String role, UUID userId, LoanRequestStatus status, int pageSize, int pageNumber);

    LoanRequestResponseDto saveLoanRequest(LoanRequestRequestDto loanRequestRequestDto, UUID userId);

    LoanRequestResponseDto changeLoanRequestInterestRateAndStatus(LoanRequestOfferDto loanRequestOfferDto, UUID userId);

    LoanRequestResponseDto changeLoanRequestStatus(LoanRequestStatusDto loanRequestStatusDto, String role, UUID userId);

    LoanRequestResponseDto changeLoanRequestStatusToAccept(
            LoanRequestAcceptRequestDto loanRequestAcceptRequestDto, String role, UUID userId);

    Long getLoanProductCountByUserIdAndLoanRequestStatus(UUID userId, LoanRequestStatus loanRequestStatus);

    Boolean isNewRequestAllowedForLoanApplicationId(UUID loanApplicationId, UUID userId);
}
