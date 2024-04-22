package com.digitallending.loanservice.model.dto.loanapplication.signeddocument;

import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SignedDocumentStatusRequestDto {

    @NotNull(message = "sign document id must be given.")
    private UUID signedDocumentId;
    @NotNull(message = "sign document status must be provided.")
    private SignedDocumentStatus signedDocumentStatus;
}
