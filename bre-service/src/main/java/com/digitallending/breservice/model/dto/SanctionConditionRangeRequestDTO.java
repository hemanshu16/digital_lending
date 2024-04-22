package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SanctionConditionRangeRequestDTO {
    @NotNull(message = "Minimum value for Range is required")
    private BigDecimal min;
    @NotNull(message = "Maximum value for Range is required")
    private BigDecimal max;
    @NotNull(message = "BRE Sub Param Type Id is required")
    private UUID breSubParamTypeId;
}
