package com.digitallending.loanservice.model.dto.loanapplication;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanApplicationRequestDto {

    private UUID loanApplicationId;

    @Min(value = 20_000,message = "your monthly can not be lesser than 20,000")
    private Long monthlyIncome; // in Rs.

    @NotBlank(message = "please provide name for the loan application")
    private String loanApplicationName;

    @Min(value = 50_000,message = "loan amount must be more than or equal to 50,000")
    private Long amount; // in Rs.

    @Positive(message = "Duration must be positive")
    private Long tenure; // in month

    @NotNull(message = "please provide Sales growth")
    private Float salesGrowth; // in percentage

    @NotNull(message = "please provide profit growth")
    private Float profitGrowth; // in percentage

    @NotNull(message = "please provide net profit")
    private Float netProfit; // in percentage

    @Min(value = 300,message = "CIBIL score can not be less than 300")
    @Max(value = 900,message = "CIBIL score can not be more than 900")
    private Integer cibilScore;

    @Max(value = 10,message = "CIBIL rank can not be more than 10")
    private Integer cibilRank;

    @NotBlank(message = "Statement of purpose must not be blank after trimming")
    @Size(max = 500, message = "statement of purpose can't be more than 500 latters")
    private String statementOfPurpose;

    @NotNull(message = "Loan type ID must not be blank after trimming")
    private UUID loanTypeId;
}
