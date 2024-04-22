package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.transaction.TransactionResponseDto;
import com.digitallending.loanservice.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "loanApplication.loanApplicationName", target = "loanApplicationName")
    @Mapping(source = "loanProduct.loanProductName", target = "loanProductName")
    TransactionResponseDto transactionToTransactionResponseDto(Transaction transaction);
}
