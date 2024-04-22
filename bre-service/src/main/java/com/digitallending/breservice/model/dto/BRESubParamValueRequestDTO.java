package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BRESubParamValueRequestDTO {

    @NotNull(message = "BRE Sub Param Type value ID is required")
    private UUID breSubParamTypeValueId;

    @NotNull(message = "Score is required")
    @Min(value = -10, message = "Score must be at least -10")
    @Max(value = 10, message = "Score must be at most 10")
    private Integer score;
}
