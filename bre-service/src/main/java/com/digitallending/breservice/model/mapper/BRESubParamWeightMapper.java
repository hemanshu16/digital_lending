package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BRESubParamWeightResponseDTO;
import com.digitallending.breservice.model.entity.BRESubParamWeight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {BRESubParamValueMapper.class,BRESubParamRangeMapper.class})
public interface BRESubParamWeightMapper {

    @Mapping(target = "breSubParamTypeId",source = "breSubParamType.subParamTypeId")
    BRESubParamWeightResponseDTO toDto(BRESubParamWeight breSubParamWeight);
}
