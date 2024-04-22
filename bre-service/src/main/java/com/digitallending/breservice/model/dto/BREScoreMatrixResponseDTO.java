package com.digitallending.breservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BREScoreMatrixResponseDTO {
    private UUID scoreId;
    private UUID loanProductId;
    private Integer scoreFrom;
    private Integer scoreTo;
    private BigDecimal minInterestRate;
    private BigDecimal maxLoanAmount;
    private Integer maxTenureInMonths;
}
