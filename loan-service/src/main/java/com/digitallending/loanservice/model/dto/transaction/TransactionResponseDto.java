package com.digitallending.loanservice.model.dto.transaction;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDto {
    private String providerAccountHolderName;
    private String receiverAccountHolderName;
    private String providerAccountNo;
    private String receiverAccountNo;
    private Long amount;
    private ZonedDateTime timestamp;
    private String loanProductName;
    private String loanApplicationName;
}
