package com.digitallending.loanservice.model.dto.externalservice.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SendTransactionRequestDto {
    private UUID userId;
}

