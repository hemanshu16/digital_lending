package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanapplication.document.DocumentResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.document.ProvidedDocumentResponseDto;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.enums.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    DocumentResponseDto getAllDocumentByLoanApplicationId(UUID loanApplicationId, UUID userId, Role role);

    Long getCountByLoanApplicationId(UUID loanApplicationId);

    String saveAndUpdateDocument(MultipartFile file, UUID documentTypeId, UUID loanApplicationId,UUID userId);

    List<ProvidedDocumentResponseDto> getAllDocumentByLoanRequestId(UUID loanRequestId, UUID userId, Role role);

    void deleteExtraDocumentBasedOnUpdatedLoanType(UUID loanApplicationId,LoanType loanType, LoanType loanType1);
}
