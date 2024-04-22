package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentResponseForAdmin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SignedDocumentMapper.class)

public interface SignedDocumentForAdminMapper {

    SignedDocumentResponseForAdmin toSignedDocumentResponseForAdmin(
            SignedDocumentResponseDto signedDocumentResponseDto,
            String lenderName,
            String MSMEName,
            Double interestRate,
            Long amount,
            Long tenure
    );
}
