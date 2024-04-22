package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BRESubParamRangeRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeResponseDTO;
import com.digitallending.breservice.model.dto.VerifyRangeConstraintDTO;
import com.digitallending.breservice.model.entity.SanctionConditionRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SanctionConditionRangeMapper {
    @Mapping(target = "breSubParamType",source = "breSubParamType.subParamTypeId")
    SanctionConditionRangeResponseDTO toDto (SanctionConditionRange sanctionConditionRange);
    VerifyRangeConstraintDTO toVerifyRangeConstraintDTO(SanctionConditionRangeRequestDTO sanctionConditionRangeRequestDTO);
}
