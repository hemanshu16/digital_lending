package com.digitallending.loanservice.model.dto.loanrequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestAcceptRequestDto {
    @NotNull(message = "Loan request id must be given.")
    private UUID loanRequestId;
    @NotNull(message = "Bank account must need to be given to create loan request.")
    private UUID receiverAccountId;
}
