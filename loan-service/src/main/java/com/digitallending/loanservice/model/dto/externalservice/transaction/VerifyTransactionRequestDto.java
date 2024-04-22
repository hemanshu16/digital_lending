package com.digitallending.loanservice.model.dto.externalservice.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyTransactionRequestDto {
    private String txnId;
    private Integer otp;
}
