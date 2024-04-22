package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class BRESubParamRangeResponseDTO {
    private UUID rangeId;
    private BigDecimal min;
    private BigDecimal max;
    private Integer score;
}
