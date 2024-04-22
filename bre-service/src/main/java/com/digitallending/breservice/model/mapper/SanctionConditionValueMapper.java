package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.SanctionConditionValueResponseDTO;
import com.digitallending.breservice.model.entity.SanctionConditionValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SanctionConditionValueMapper {
    @Mapping(target = "breSubParamTypeId",source = "breSubParamType.subParamTypeId")
    @Mapping(target = "breSubParamTypeValueId",source = "breSubParamTypeValue.breSubParamTypeValueId")
    SanctionConditionValueResponseDTO toDto (SanctionConditionValue sanctionConditionValue);
}
