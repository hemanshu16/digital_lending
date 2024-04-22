package com.digitallending.loanservice.model.dto.loanapplication.signeddocument;

import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class SignedDocumentResponseDto {
    private UUID signedDocumentId;
    private SignedDocumentStatus status;
    private byte[] documentContent;
    private UUID loanApplicationId;
}
