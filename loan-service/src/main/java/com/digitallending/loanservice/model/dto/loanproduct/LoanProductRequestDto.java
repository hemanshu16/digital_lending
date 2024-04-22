package com.digitallending.loanservice.model.dto.loanproduct;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanProductRequestDto {
    @NotNull(message = "please select user")
    private UUID userId;
    @NotNull(message = "please select loan type")
    private UUID loanTypeId;
    @PositiveOrZero(message = "Maximum amount must be a positive number or zero")
    private Long maxAmount;
    @PositiveOrZero(message = "Maximum tenure must be a positive number or zero")
    private Long maxTenure;
    @NotBlank(message = "please provide loan product name")
    private String loanProductName;
    @NotBlank(message = "please provide loan product description")
    private String loanProductDescription;
}
