package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BRESubParamValueResponseDTO;
import com.digitallending.breservice.model.entity.BRESubParamValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BRESubParamValueMapper {

    @Mapping(target = "breSubParamTypeValueId",source = "breSubParamTypeValue.breSubParamTypeValueId")
    BRESubParamValueResponseDTO toDto(BRESubParamValue breSubParamValue);

}
