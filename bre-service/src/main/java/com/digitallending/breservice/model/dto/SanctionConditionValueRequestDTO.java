package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SanctionConditionValueRequestDTO {
    @NotNull(message = "BRE sub-param type value ID is required")
    private UUID breSubParamTypeValueId;
    @NotNull(message = "Sub-param type ID is required")
    private UUID subParamTypeId;
}
