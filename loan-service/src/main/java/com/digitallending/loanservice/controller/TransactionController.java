package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.transaction.InitiateTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.SubmitTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionPaginationResponseDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionResponseDto;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.service.def.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/loan/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    //  ADMIN
    @GetMapping
    public ResponseEntity<ApiResponse<TransactionPaginationResponseDto>> getAllTransaction(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<TransactionPaginationResponseDto> apiResponse = ApiResponse
                .<TransactionPaginationResponseDto>builder()
                .payload(transactionService.getAllTransaction(pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  msme,admin,lender
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<TransactionPaginationResponseDto>> getAllTransactionByUser(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestHeader("UserId") UUID headerUserId,
            @RequestHeader("Role") Role role,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<TransactionPaginationResponseDto> apiResponse = ApiResponse
                .<TransactionPaginationResponseDto>builder()
                .payload(transactionService.getAllTransactionByUserId(userId, headerUserId, role,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  msme,admin
    @GetMapping("/loan-application/{loanApplicationId}")
    public ResponseEntity<ApiResponse<TransactionResponseDto>> getTransactionByLoanApplicationId(@PathVariable UUID loanApplicationId){
        ApiResponse<TransactionResponseDto> apiResponse = ApiResponse
                .<TransactionResponseDto>builder()
                .payload(transactionService.getTransactionByLoanApplicationId(loanApplicationId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  lender,admin
    @GetMapping("/loan-product/{loanProductId}")
    public ResponseEntity<ApiResponse<TransactionPaginationResponseDto>> getTransactionByLoanProductId(
            @PathVariable UUID loanProductId,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<TransactionPaginationResponseDto> apiResponse = ApiResponse
                .<TransactionPaginationResponseDto>builder()
                .payload(transactionService.getTransactionByLoanProductId(loanProductId,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  LENDER
    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<String>> initiateTransaction(
            @RequestHeader("UserId") UUID userId,
            @Valid @RequestBody InitiateTransactionRequestDto initiateTransactionRequestDto) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(transactionService.initiateTransaction(initiateTransactionRequestDto, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  Lender
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<String>> completeTransaction(
            @RequestHeader("UserId") UUID userId,
            @Valid @RequestBody SubmitTransactionRequestDto submitTransactionRequestDto) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(transactionService.submitTransaction(userId, submitTransactionRequestDto))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
