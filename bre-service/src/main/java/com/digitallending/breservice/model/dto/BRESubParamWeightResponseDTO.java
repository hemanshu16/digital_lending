package com.digitallending.breservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BRESubParamWeightResponseDTO {
    private UUID subParamWeightId;
    private UUID breSubParamTypeId;
    private Integer weight;
    private List<BRESubParamValueResponseDTO> breSubParamValueList = new ArrayList<>();
    private List<BRESubParamRangeResponseDTO> breSubParamRangeList = new ArrayList<>();
}
