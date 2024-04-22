package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.LoanTypeResponseDto;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.service.def.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loan/loan-type")
public class LoanTypeController {

    @Autowired
    private LoanTypeService loanTypeService;

    //    MSME
    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanTypeResponseDto>>> getAllLoanType(){
        ApiResponse<List<LoanTypeResponseDto>> apiResponse = ApiResponse
                .<List<LoanTypeResponseDto>>builder()
                .payload(loanTypeService.getAllLoanType())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
