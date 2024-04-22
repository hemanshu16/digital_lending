package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BREParamWeightResponseDTO {
    private UUID paramWeightId;
    private UUID loanProductId;
    private UUID paramTypeId;
    private Integer weight;
}