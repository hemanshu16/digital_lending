package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentResponseDto;
import com.digitallending.loanservice.model.entity.SignedDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignedDocumentMapper {
    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    SignedDocumentResponseDto toSignedDocumentResponseDto(SignedDocument signedDocument);
}
