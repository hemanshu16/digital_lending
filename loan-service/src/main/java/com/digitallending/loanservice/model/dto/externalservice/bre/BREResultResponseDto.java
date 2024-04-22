package com.digitallending.loanservice.model.dto.externalservice.bre;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BREResultResponseDto {
    private UUID loanProductId;
    private String loanProductName;
    private String loanProductDescription;
    private Integer score;
    private Double minInterestRate;
}
