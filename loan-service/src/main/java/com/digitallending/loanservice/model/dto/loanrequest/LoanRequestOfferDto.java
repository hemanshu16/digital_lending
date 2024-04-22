package com.digitallending.loanservice.model.dto.loanrequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestOfferDto {

    @NotNull(message = "Loan request id must be required.")
    private UUID loanRequestId;
    @NotNull(message = "Interest rate must be given.")
    @PositiveOrZero(message = "Interest rate must be positive or zero.")
    private Double interestRate;
}
