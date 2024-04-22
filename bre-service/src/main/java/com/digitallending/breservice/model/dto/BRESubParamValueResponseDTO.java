package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BRESubParamValueResponseDTO {
    private UUID valueId;
    private UUID breSubParamTypeValueId;
    private Integer score;
}
