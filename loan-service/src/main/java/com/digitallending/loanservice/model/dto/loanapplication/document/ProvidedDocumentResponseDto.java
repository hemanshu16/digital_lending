package com.digitallending.loanservice.model.dto.loanapplication.document;

import com.digitallending.loanservice.model.entity.DocumentType;
import lombok.Data;

import java.util.UUID;

@Data
public class ProvidedDocumentResponseDto {
    private UUID documentId;
    private DocumentType documentType;
    private byte[] documentContent;
}
