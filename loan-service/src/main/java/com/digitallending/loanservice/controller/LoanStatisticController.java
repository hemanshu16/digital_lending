package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanstatistic.LenderStatisticDto;
import com.digitallending.loanservice.model.dto.loanstatistic.AdminStatisticDto;
import com.digitallending.loanservice.service.def.LoanStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/loan/loan-statistics")
public class LoanStatisticController {
    @Autowired
    private LoanStatisticService loanStatisticService;

    //  ADMIN
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<AdminStatisticDto>> getLoanStatisticForAdmin() {
        ApiResponse<AdminStatisticDto> apiResponse = ApiResponse
                .<AdminStatisticDto>builder()
                .payload(loanStatisticService.getLoanStatisticForAdmin())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  LENDER
    @GetMapping("/lender")
    public ResponseEntity<ApiResponse<LenderStatisticDto>> getAllLoanProductStatistic(
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<LenderStatisticDto> apiResponse = ApiResponse
                .<LenderStatisticDto>builder()
                .payload(loanStatisticService.getLoanStatisticForLender(userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
