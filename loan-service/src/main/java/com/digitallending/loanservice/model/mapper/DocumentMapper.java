package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanapplication.document.ProvidedDocumentResponseDto;
import com.digitallending.loanservice.model.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = DocumentTypeMapper.class)
public interface DocumentMapper {
    ProvidedDocumentResponseDto toResponseDto(Document document);
}