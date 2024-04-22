package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestAcceptRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestOfferDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanProductDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestStatusDto;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.service.def.LoanRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan/loan-request")
public class LoanRequestController {

    @Autowired
    private LoanRequestService loanRequestService;

    @GetMapping("/{loanRequestId}")
    public ResponseEntity<ApiResponse<LoanRequestResponseDto>> getLoanRequestById(
            @PathVariable UUID loanRequestId,
            @RequestHeader("UserId") UUID userId,
            @RequestHeader("Role") Role role){
        ApiResponse<LoanRequestResponseDto> apiResponse = ApiResponse
                .<LoanRequestResponseDto>builder()
                .payload(loanRequestService.getLoanRequestResponseDtoById(loanRequestId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //msme
    @GetMapping("/loan-application/{loanApplicationId}/new-request-allowed")
    public ResponseEntity<ApiResponse<Boolean>> isNewRequestAllowedForLoanApplicationId(
            @PathVariable UUID loanApplicationId,
            @RequestHeader("UserId") UUID userId){
        ApiResponse<Boolean> apiResponse = ApiResponse
                .<Boolean>builder()
                .payload(loanRequestService.isNewRequestAllowedForLoanApplicationId(loanApplicationId, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    ADMIN,MSME
    @GetMapping("/loan-application/{loanApplicationId}")
    public ResponseEntity<ApiResponse<List<LoanRequestResponseWithLoanProductDto>>> getAllLoanRequestByLoanApplicationIdAndStatus(
            @PathVariable UUID loanApplicationId,
            @RequestHeader("Role") String role,
            @RequestHeader("UserId") UUID userId,
            @RequestParam("status") LoanRequestStatus status) {


        ApiResponse<List<LoanRequestResponseWithLoanProductDto>> apiResponse = ApiResponse
                .<List<LoanRequestResponseWithLoanProductDto>>builder()
                .payload(loanRequestService.getAllLoanRequestByLoanApplicationIdAndStatus(loanApplicationId, role, userId, status))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    ADMIN,LENDER
    @GetMapping("/loan-product/{loanProductId}")
    public ResponseEntity<ApiResponse<LoanRequestPaginationResponseDto>> getAllLoanRequestByLoanProductIdAndStatus(
            @PathVariable UUID loanProductId,
            @RequestHeader("Role") String role,
            @RequestHeader("UserId") UUID userId,
            @RequestParam("status") LoanRequestStatus status,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber) {

        ApiResponse<LoanRequestPaginationResponseDto> apiResponse = ApiResponse
                .<LoanRequestPaginationResponseDto>builder()
                .payload(loanRequestService.getAllLoanRequestByLoanProductIdAndStatus(loanProductId, role, userId, status, pageSize, pageNumber))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    MSME
    @PostMapping
    public ResponseEntity<ApiResponse<LoanRequestResponseDto>> createLoanRequest(
            @Valid @RequestBody LoanRequestRequestDto loanRequestRequestDto,
            @RequestHeader("UserId") UUID userId) {

        ApiResponse<LoanRequestResponseDto> apiResponse = ApiResponse
                .<LoanRequestResponseDto>builder()
                .payload(loanRequestService.saveLoanRequest(loanRequestRequestDto, userId))
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    //    lender
    @PatchMapping("/offered")
    public ResponseEntity<ApiResponse<LoanRequestResponseDto>> changeLoanRequestInterestRateAndStatus(
            @Valid @RequestBody LoanRequestOfferDto loanRequestOfferDto,
            @RequestHeader("UserId") UUID userId) {

        ApiResponse<LoanRequestResponseDto> apiResponse = ApiResponse
                .<LoanRequestResponseDto>builder()
                .payload(loanRequestService.changeLoanRequestInterestRateAndStatus(loanRequestOfferDto, userId))
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    MSME,LENDER
    @PatchMapping("/change-status")
    public ResponseEntity<ApiResponse<LoanRequestResponseDto>> changeLoanRequestStatus(
            @Valid @RequestBody LoanRequestStatusDto loanRequestStatusDto,
            @RequestHeader("Role") String role,
            @RequestHeader("UserId") UUID userId) {

        ApiResponse<LoanRequestResponseDto> apiResponse = ApiResponse
                .<LoanRequestResponseDto>builder()
                .payload(loanRequestService.changeLoanRequestStatus(loanRequestStatusDto, role, userId))
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/accept")
    public ResponseEntity<ApiResponse<LoanRequestResponseDto>> changeLoanRequestStatusToAccept(
            @Valid @RequestBody LoanRequestAcceptRequestDto loanRequestAcceptRequestDto,
            @RequestHeader("Role") String role,
            @RequestHeader("UserId") UUID userId) {

        ApiResponse<LoanRequestResponseDto> apiResponse = ApiResponse
                .<LoanRequestResponseDto>builder()
                .payload(loanRequestService.changeLoanRequestStatusToAccept(loanRequestAcceptRequestDto, role, userId))
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
