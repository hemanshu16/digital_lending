package com.digitallending.breservice.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BREParamTypeResponseDTO {
    private UUID paramTypeId;
    private String paramName;
}
