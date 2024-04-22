package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationStatusRequestDto;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.repository.LoanTypeRepository;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/api/loan/loan-application")
public class LoanApplicationController {
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LoanTypeRepository loanTypeRepository;


    //    MSME,ADMIN
    @GetMapping("/{loanApplicationId}")
    public ResponseEntity<ApiResponse<LoanApplicationResponseDto>> getLoanApplicationByLoanApplicationId(
            @PathVariable UUID loanApplicationId,
            @RequestHeader("Role") Role role,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<LoanApplicationResponseDto> apiResponse = ApiResponse
                .<LoanApplicationResponseDto>builder()
                .payload(loanApplicationService.getLoanApplicationByLoanApplicationId(
                        loanApplicationId,
                        role,
                        userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    lender,admin
    @GetMapping("/loan-request/{loanRequestId}")
    public ResponseEntity<ApiResponse<LoanApplicationResponseDto>> getLoanApplicationByLoanRequestId(
            @PathVariable UUID loanRequestId,
            @RequestHeader("Role") Role role,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<LoanApplicationResponseDto> apiResponse = ApiResponse
                .<LoanApplicationResponseDto>builder()
                .payload(loanApplicationService.getLoanApplicationByLoanRequestId(
                        loanRequestId,
                        userId,
                        role))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    MSME,ADMIN
    @GetMapping
    @Transactional
    public ResponseEntity<ApiResponse<LoanApplicationPaginationResponseDto>> getFilteredLoanApplication(
            @RequestHeader("UserId") UUID userId,
            @RequestHeader("Role") Role role,
            @RequestParam(value="loanApplicationStatus",required = false)LoanApplicationStatus loanApplicationStatus,
            @RequestParam(value = "msmeUserId", required = false) UUID msmeUserId,
            @RequestParam(value = "loanTypeId",required = false) UUID loanTypeId,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<LoanApplicationPaginationResponseDto> apiResponse = ApiResponse
                .<LoanApplicationPaginationResponseDto>builder()
                .payload(loanApplicationService
                        .getFilteredLoanApplication(userId, role,msmeUserId,loanApplicationStatus,loanTypeId,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  msme
    @PatchMapping("/submit")
    public ResponseEntity<ApiResponse<String>> submitLoanApplication(
            @RequestParam("loanApplicationId") UUID loanApplicationId,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(loanApplicationService.submitLoanApplication(loanApplicationId, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  MSME
    @PostMapping
    public ResponseEntity<ApiResponse<LoanApplicationResponseDto>> saveLoanApplication(
            @RequestBody @Valid LoanApplicationRequestDto loanApplicationRequestDto,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<LoanApplicationResponseDto> apiResponse = ApiResponse
                .<LoanApplicationResponseDto>builder()
                .payload(loanApplicationService.saveLoanApplication(loanApplicationRequestDto, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    //    ADMIN
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<String>> updateLoanApplicationStatus(
            @RequestBody @Valid LoanApplicationStatusRequestDto loanApplicationStatusRequestDto) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(loanApplicationService.updateLoanApplicationStatus(loanApplicationStatusRequestDto))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{loanApplicationId}/sign-document")
    public ResponseEntity<ApiResponse<byte[]>> getSignDocumentContentByLoanApplicationId(
            @PathVariable UUID loanApplicationId,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<byte[]> apiResponse = ApiResponse
                .<byte[]>builder()
                .payload(loanApplicationService.getSignDocumentContentByLoanApplicationId(
                        loanApplicationId, userId))
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}