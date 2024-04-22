package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BREParamAndSubParamWeightResponseDTO {
    private UUID paramWeightId;
    private UUID loanProductId;
    private UUID paramTypeId;
    private Integer weight;
    private List<BRESubParamWeightResponseDTO> breSubParamWeightsList;
}
