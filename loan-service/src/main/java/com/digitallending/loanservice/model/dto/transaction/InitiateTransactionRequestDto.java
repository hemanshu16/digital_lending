package com.digitallending.loanservice.model.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InitiateTransactionRequestDto {
    @NotNull(message = "please provide request id")
    private UUID historyLoanRequestId;
    @NotNull(message = "please provide your account id")
    private UUID providerAccountId;
}
