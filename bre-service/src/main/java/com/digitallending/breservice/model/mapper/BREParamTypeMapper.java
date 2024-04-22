package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BREParamTypeResponseDTO;
import com.digitallending.breservice.model.entity.BREParamType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BREParamTypeMapper {
    BREParamTypeResponseDTO toDto(BREParamType breParamType);
}
