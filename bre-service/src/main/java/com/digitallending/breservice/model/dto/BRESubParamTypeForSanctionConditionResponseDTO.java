package com.digitallending.breservice.model.dto;

import com.digitallending.breservice.model.entity.BRESubParamType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BRESubParamTypeForSanctionConditionResponseDTO {
    private List<BRESubParamType> breSubParamTypeValueList;
    private List<BRESubParamType> breSubParamTypeRangeList;
}
