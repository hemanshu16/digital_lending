package com.digitallending.breservice.model.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class SanctionConditionValueResponseDTO {
    private UUID id;
    private UUID loanProductId;
    private UUID breSubParamTypeValueId;
    private UUID breSubParamTypeId;
}
