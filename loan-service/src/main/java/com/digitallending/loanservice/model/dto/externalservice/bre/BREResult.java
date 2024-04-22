package com.digitallending.loanservice.model.dto.externalservice.bre;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BREResult {
    private UUID loanProductId;
    private Integer score;
    private Double minInterestRate;
}
