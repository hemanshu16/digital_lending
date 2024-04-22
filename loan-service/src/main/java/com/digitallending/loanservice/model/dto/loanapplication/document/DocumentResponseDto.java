package com.digitallending.loanservice.model.dto.loanapplication.document;

import com.digitallending.loanservice.model.entity.DocumentType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentResponseDto {
    List<ProvidedDocumentResponseDto> providedDocumentResponseDtoList;
    List<DocumentType> requiredDocumentTypeList;
}
