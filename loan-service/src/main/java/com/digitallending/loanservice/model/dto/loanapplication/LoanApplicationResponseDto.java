package com.digitallending.loanservice.model.dto.loanapplication;

import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentResponseDto;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanApplicationResponseDto {
    private UUID loanApplicationId;
    private UUID userId;
    private String userName;
    private String loanApplicationName;
    private Long monthlyIncome;
    private String loanTypeName;
    private Long amount;
    private Long tenure;
    private String statementOfPurpose;
    private Float salesGrowth;
    private Float profitGrowth;
    private Float netProfit;
    private Integer cibilScore;
    private Integer cibilRank;
    // if loan has PropertyLoanApplication then it will be assigned over here otherwise this field will be null
    private PropertyDetailsResponseDto propertyDetailsResponseDto;
    private LoanApplicationStage loanApplicationStage;
    private SignedDocumentResponseDto signedDocumentResponseDto;
    private LoanApplicationStatus loanApplicationStatus;
}
