package com.digitallending.breservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class BREScoreMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID scoreId;
    private UUID loanProductId;
    private Integer scoreFrom;
    private Integer scoreTo;
    private BigDecimal minInterestRate;
    private BigDecimal maxLoanAmount;
    private Integer maxTenureInMonths;

}
