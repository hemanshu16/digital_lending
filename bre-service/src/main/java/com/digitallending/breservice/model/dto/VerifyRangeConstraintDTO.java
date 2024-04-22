package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VerifyRangeConstraintDTO {
    private BigDecimal min;
    private BigDecimal max;
}
