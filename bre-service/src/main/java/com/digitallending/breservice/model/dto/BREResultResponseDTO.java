package com.digitallending.breservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class BREResultResponseDTO {
    private UUID loanProductId;
    private Integer score;
    private BigDecimal minInterestRate;
}
