package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanapplication.document.DocumentResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.document.ProvidedDocumentResponseDto;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.service.def.DocumentService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan/loan-application/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    //  ADMIN,MSME
    @GetMapping
    public ResponseEntity<ApiResponse<DocumentResponseDto>> getAllDocumentByLoanApplicationId(
            @RequestParam("loanApplicationId") UUID loanApplicationId,
            @RequestHeader("UserId") UUID userId,
            @RequestHeader("Role") Role role) {
        ApiResponse<DocumentResponseDto> apiResponse = ApiResponse
                .<DocumentResponseDto>builder()
                .payload(documentService.getAllDocumentByLoanApplicationId(loanApplicationId, userId, role))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
//  ADMIN,LENDER
    @GetMapping("/loan-request")
    public ResponseEntity<ApiResponse<List<ProvidedDocumentResponseDto>>> getAllDocumentByLoanRequestId(
            @RequestParam("loanRequestId") UUID loanRequestId,
            @RequestHeader("UserId") UUID userId,
            @RequestHeader("Role") Role role) {
        ApiResponse<List<ProvidedDocumentResponseDto>> apiResponse = ApiResponse
                .<List<ProvidedDocumentResponseDto>>builder()
                .payload(documentService.getAllDocumentByLoanRequestId(loanRequestId, userId, role))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

//  MSME
    @PostMapping
    public ResponseEntity<ApiResponse<String>> saveAndUpdateDocument(
            @NotNull @RequestParam MultipartFile file,
            @NotNull @RequestParam UUID documentTypeId,
            @NotNull @RequestParam UUID loanApplicationId,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(documentService.saveAndUpdateDocument(file, documentTypeId, loanApplicationId, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
