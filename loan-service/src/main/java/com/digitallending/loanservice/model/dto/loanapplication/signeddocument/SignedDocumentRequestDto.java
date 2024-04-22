package com.digitallending.loanservice.model.dto.loanapplication.signeddocument;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SignedDocumentRequestDto {
    @NotNull(message = "loan request id must be given.")
    private UUID loanRequestId;
}
