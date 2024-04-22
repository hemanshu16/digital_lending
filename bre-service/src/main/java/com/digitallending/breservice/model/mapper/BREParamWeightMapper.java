package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BREParamAndSubParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREParamWeightResponseDTO;
import com.digitallending.breservice.model.entity.BREParamWeight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {BRESubParamWeightMapper.class})
public interface BREParamWeightMapper {
    @Mapping(target = "paramTypeId",source = "paramType.paramTypeId")
    BREParamWeightResponseDTO toDto (BREParamWeight breParamWeight);

    @Mapping(target = "paramTypeId",source = "paramType.paramTypeId")
    BREParamAndSubParamWeightResponseDTO toDtoAfterSave (BREParamWeight breParamWeight);
}