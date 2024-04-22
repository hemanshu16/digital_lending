package com.digitallending.breservice.model.mapper;

import com.digitallending.breservice.model.dto.BRESubParamRangeRequestDTO;
import com.digitallending.breservice.model.dto.VerifyRangeConstraintDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BRESubParamRangeMapper {
    VerifyRangeConstraintDTO toDTO(BRESubParamRangeRequestDTO breSubParamRangeRequestDTO);

}
