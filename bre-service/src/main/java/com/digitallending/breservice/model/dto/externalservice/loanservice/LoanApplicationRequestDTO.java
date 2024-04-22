package com.digitallending.breservice.model.dto.externalservice.loanservice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequestDTO {

    @NotNull(message = "Monthly income is required")
    @Min(value = 20000, message = "Monthly income must be greater than or equal to 20000")
    private BigDecimal monthlyIncome;

    private BigDecimal ownershipVintage;
    @Min(value = 20000, message = "Ownership vintage must be greater than or equal to 20000")
    private BigDecimal propertyValuation;
    private String propertyType;

    @NotNull(message = "Net profit is required")
    private BigDecimal netProfit;

    @NotNull(message = "CIBIL score is required")
    @Range(min = 300, max = 900, message = "CIBIL score must be between 300 and 900")
    private BigDecimal cibilScore;

    @NotNull(message = "CIBIL rank is required")
    @Range(min = 1, max = 10, message = "CIBIL rank must be between 1 and 10")
    private BigDecimal cibilRank;

    @NotNull(message = "Sales growth is required")
    private BigDecimal salesGrowth;

    @NotNull(message = "Profit growth is required")
    private BigDecimal profitGrowth;

    @NotNull(message = "Amount is required")
    @Min(value = 50000, message = "Amount must be greater than or equal to 50000")
    private BigDecimal amount;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Tenure must be greater than or equal to 1")
    private Integer tenure;
}
