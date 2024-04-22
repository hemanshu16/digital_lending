package com.digitallending.userservice.model.dto.business;

import lombok.Data;

import java.util.UUID;

@Data
public class BusinessTypeDTO {

    private UUID businessTypeId;

    private String businessType;
}
