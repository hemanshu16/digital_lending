package com.digitallending.loanservice.model.dto.transaction;

import com.digitallending.loanservice.model.dto.PaginationResponseDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class TransactionPaginationResponseDto extends PaginationResponseDto {
    private List<TransactionResponseDto> transactionResponseDtoList;
}
