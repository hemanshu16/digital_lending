package com.digitallending.breservice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BRESubParamRangeRequestDTO {

    @NotNull(message = "Min value is required")
    private BigDecimal min;

    @NotNull(message = "Max value is required")
    private BigDecimal max;

    @NotNull(message = "Score is required")
    @Min(value = -10, message = "Score must be at least -10")
    @Max(value = 10, message = "Score must be at most 10")
    private Integer score;
}
