package com.digitallending.loanservice.model.dto.externalservice.bre;

import lombok.Data;

@Data
public class LoanApplicationBRERequestDto {
    private Long monthlyIncome;
    private Long amount;
    private Long tenure;
    private Long salesGrowth;
    private Long profitGrowth;
    private Long netProfit;
    private Long cibilScore;
    private Long cibilRank;

    private Integer ownershipVintage;
    private Long propertyValuation;
    private String propertyType;
}
