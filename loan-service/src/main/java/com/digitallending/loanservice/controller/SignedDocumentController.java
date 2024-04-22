package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentStatusRequestDto;
import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.service.def.SignedDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan/signed-document")
public class SignedDocumentController {

    @Autowired
    private SignedDocumentService signedDocumentService;

    //    admin
    @GetMapping("/unapproved-signed-document")
    public ResponseEntity<ApiResponse<SignedDocumentPaginationResponseDto>> getAllUnapprovedSignDocument(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
    ) {

        ApiResponse<SignedDocumentPaginationResponseDto> apiResponse = ApiResponse
                .<SignedDocumentPaginationResponseDto>builder()
                .payload(signedDocumentService.getAllUnapprovedSignDocument(pageSize, pageNumber))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //    Admin
    @PatchMapping("/change-status")
    public ResponseEntity<ApiResponse<String>> changeSignedDocumentStatus(
            @Valid @RequestBody SignedDocumentStatusRequestDto signedDocumentStatusRequestDto) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(signedDocumentService.changeSignedDocumentStatus(signedDocumentStatusRequestDto))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //  lender,msme
    @PatchMapping("/upload-signed-document")
    public ResponseEntity<ApiResponse<String>> uploadSignedDocument(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "signDocument") @Valid SignedDocumentRequestDto signedDocumentRequestDto,
            @RequestHeader("Role") String role,
            @RequestHeader("UserId") UUID userId
    ) {
        ApiResponse<String> apiResponse = ApiResponse
                .<String>builder()
                .payload(signedDocumentService.uploadSignedDocument(signedDocumentRequestDto, file, role, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
