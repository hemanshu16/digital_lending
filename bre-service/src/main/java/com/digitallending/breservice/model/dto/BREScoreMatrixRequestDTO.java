package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BREScoreMatrixRequestDTO {

    @NotNull(message = "Score From is required")
    @Min(value = -10, message = "Score From must be at least -10")
    @Max(value = 10, message = "Score From must be at most 10")
    private Integer scoreFrom;

    @NotNull(message = "Score To is required")
    @Min(value = -10, message = "Score To must be at least -10")
    @Max(value = 10, message = "Score To must be at most 10")
    private Integer scoreTo;

    @NotNull(message = "Minimum Interest Rate is required")
    @Min(value = 0, message = "Minimum Interest Rate must be at least 0")
    @Max(value = 100, message = "Minimum Interest Rate must be at most 100")
    private BigDecimal minInterestRate;

    @NotNull(message = "Maximum Loan Amount is required")
    @Min(value = 1, message = "Maximum Loan Amount must be at least 1")
    private BigDecimal maxLoanAmount;

    @NotNull(message = "Maximum Tenure In Months is required")
    @Min(value = 1, message = "Maximum Tenure In Months must be at least 1")
    private Integer maxTenureInMonths;
}
