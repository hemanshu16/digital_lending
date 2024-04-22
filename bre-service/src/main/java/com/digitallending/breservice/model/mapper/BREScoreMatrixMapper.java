package com.digitallending.breservice.model.mapper;


import com.digitallending.breservice.model.dto.BREScoreMatrixResponseDTO;
import com.digitallending.breservice.model.entity.BREScoreMatrix;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BREScoreMatrixMapper {
    BREScoreMatrixResponseDTO toDto (BREScoreMatrix breScoreMatrix);
}