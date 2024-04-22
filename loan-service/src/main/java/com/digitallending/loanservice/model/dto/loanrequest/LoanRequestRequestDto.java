package com.digitallending.loanservice.model.dto.loanrequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestRequestDto {

    @NotNull(message = "Loan product must need to be given to create loan request.")
    private UUID loanProductId;
    @NotNull(message = "Loan application must need to be given to create loan request.")
    private UUID loanApplicationId;
}
