package com.digitallending.loanservice.model.dto.loanapplication.signeddocument;

import lombok.Data;

@Data
public class SignedDocumentResponseForAdmin extends SignedDocumentResponseDto{
    private String lenderName;
    private String MSMEName;
    private Double interestRate;
    private Long tenure;
    private Long amount;
}
