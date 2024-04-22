package com.digitallending.loanservice.model.dto.loanrequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestStatusDto {

    @NotNull(message = "Loan request id must be required.")
    private UUID loanRequestId;
}
