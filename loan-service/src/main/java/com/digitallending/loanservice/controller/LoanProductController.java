package com.digitallending.loanservice.controller;


import com.digitallending.loanservice.model.dto.BREResultPaginationResponseDto;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResultResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductRequestDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductResponseDto;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.service.def.LoanProductService;
import com.digitallending.loanservice.service.def.LoanTypeService;
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
@RequestMapping("/api/loan/loan-product")
public class LoanProductController {
    @Autowired
    private LoanProductService loanProductService;

    @Autowired
    private LoanTypeService loanTypeService;

    //    ADMIN,LENDER
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<LoanProductResponseDto>> getLoanProductByProductId(
            @PathVariable UUID productId,
            @RequestHeader("Role") Role role,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<LoanProductResponseDto> apiResponse = ApiResponse
                .<LoanProductResponseDto>builder()
                .payload(loanProductService.getLoanProductByProductId(productId, userId, role))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    //    ADMIN,LENDER
    @GetMapping
    public ResponseEntity<ApiResponse<LoanProductPaginationResponseDto>> getFilteredLoanProduct(
            @RequestHeader("Role") Role role,
            @RequestHeader("UserId") UUID userId,
            @RequestParam(value = "lenderId", required = false) UUID lenderId,
            @RequestParam(value = "loanTypeId", required = false) UUID loanTypeId,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<LoanProductPaginationResponseDto> apiResponse = ApiResponse
                .<LoanProductPaginationResponseDto>builder()
                .payload(loanProductService.getFilteredLoanProduct(userId, role, lenderId,loanTypeId,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/applicable/{loanApplicationId}")
    public ResponseEntity<ApiResponse<BREResultPaginationResponseDto>> showApplicableLoanProductByApplicationId(
            @PathVariable UUID loanApplicationId,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo) {
        ApiResponse<BREResultPaginationResponseDto> apiResponse = ApiResponse
                .<BREResultPaginationResponseDto>builder()
                .payload(loanProductService.showApplicableLoanProductByApplicationId(loanApplicationId,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  BRE
    @GetMapping("/{loanProductId}/loan-type-id")
    public ResponseEntity<ApiResponse<UUID>> getLoanTypeIdByLoanProductId(@PathVariable UUID loanProductId) {
        ApiResponse<UUID> apiResponse = ApiResponse
                .<UUID>builder()
                .payload(loanProductService.getLoanTypeIdByLoanProductId(loanProductId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    //    ADMIN
    @PostMapping
    public ResponseEntity<ApiResponse<LoanProductResponseDto>> saveLoanProduct(
            @Valid @RequestBody LoanProductRequestDto loanProductRequestDto) {
        ApiResponse<LoanProductResponseDto> apiResponse = ApiResponse
                .<LoanProductResponseDto>builder()
                .payload(loanProductService.saveLoanProduct(loanProductRequestDto))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}