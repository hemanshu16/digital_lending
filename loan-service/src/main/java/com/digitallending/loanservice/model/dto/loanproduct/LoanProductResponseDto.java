package com.digitallending.loanservice.model.dto.loanproduct;

import lombok.Data;

import java.util.UUID;

@Data
public class LoanProductResponseDto {
    private UUID loanProductId;
    private UUID userId;
    private String userName;
    private String loanTypeName;
    private Long maxAmount;
    private Long maxTenure;
    private String loanProductName;
    private String loanProductDescription;
}
