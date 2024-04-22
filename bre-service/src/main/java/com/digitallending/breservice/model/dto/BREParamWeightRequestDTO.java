package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BREParamWeightRequestDTO {

    @NotNull(message = "Param type ID is required")
    private UUID paramTypeId;

    @NotNull(message = "Weight is required")
    @Min(value = 0, message = "Weight must be at least 0")
    @Max(value = 100, message = "Weight must be at most 100")
    private Integer weight;
}
