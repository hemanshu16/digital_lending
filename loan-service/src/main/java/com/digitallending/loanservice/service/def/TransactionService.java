package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.transaction.InitiateTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.SubmitTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionPaginationResponseDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionResponseDto;
import com.digitallending.loanservice.model.enums.Role;

import java.util.UUID;

public interface TransactionService {
    TransactionPaginationResponseDto getAllTransaction(int pageNo,int pageSize);

    TransactionPaginationResponseDto getAllTransactionByUserId(UUID userId, UUID headerUserId, Role role,int pageNo,int pageSize);

    TransactionResponseDto getTransactionByLoanApplicationId(UUID loanApplicationId);

    TransactionPaginationResponseDto getTransactionByLoanProductId(UUID loanProductId,int pageNo,int pageSize);

    String initiateTransaction(InitiateTransactionRequestDto initiateTransactionRequestDto, UUID userId);

    String submitTransaction(UUID userId, SubmitTransactionRequestDto submitTransactionRequestDto);
}
