package com.digitallending.loanservice.model.dto.externalservice;

import lombok.Data;

@Data
public class BankAccountResponseDto {
    private String accountHolderName;
    private String accountNumber;
}
