package com.digitallending.breservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SanctionConditionRangeResponseDTO {
    private UUID id;
    private UUID loanProductId;
    private BigDecimal min;
    private BigDecimal max;
    private UUID breSubParamType;
}
