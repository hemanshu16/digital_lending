package com.digitallending.loanservice.model.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SubmitTransactionRequestDto {
    @NotNull(message = "please provide history loan request id")
    private UUID historyLoanRequestId;
    @NotNull(message = "please provide otp")
    private Integer otp;
}
