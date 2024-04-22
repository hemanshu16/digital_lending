package com.digitallending.loanservice.model.dto.loanapplication.signeddocument;

import com.digitallending.loanservice.model.dto.PaginationResponseDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class SignedDocumentPaginationResponseDto extends PaginationResponseDto {
    private List<SignedDocumentResponseForAdmin> signedDocumentResponseForAdminList;
}

