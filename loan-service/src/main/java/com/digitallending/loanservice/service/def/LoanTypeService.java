package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.LoanTypeResponseDto;
import com.digitallending.loanservice.model.entity.DocumentType;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.enums.LoanTypeName;

import java.util.List;
import java.util.UUID;

public interface LoanTypeService {
    LoanType getLoanTypeById(UUID loanTypeId);
    List<LoanTypeResponseDto> getAllLoanType();
    List<DocumentType> getAllRequiredDocumentByLoanTypeId(UUID loanTypeId);
}
