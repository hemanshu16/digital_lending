package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentStatusRequestDto;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.entity.SignedDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface SignedDocumentService {

    SignedDocument generateSignedDocument(LoanRequest loanRequest,LoanApplication loanApplication, LoanProduct loanProduct);

    String uploadSignedDocument(
            SignedDocumentRequestDto signedDocumentRequestDto,
            MultipartFile file,
            String role,
            UUID userId);

    SignedDocumentPaginationResponseDto getAllUnapprovedSignDocument(int pageSize, int pageNumber);

    String changeSignedDocumentStatus(SignedDocumentStatusRequestDto signedDocumentStatusRequestDto);
}
