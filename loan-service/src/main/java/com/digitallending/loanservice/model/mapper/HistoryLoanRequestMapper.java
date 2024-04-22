package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryLoanRequestMapper {

    @Mapping(source = "loanProduct.loanProductId", target = "loanProductId")
    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    LoanRequestResponseDto toDto(HistoryLoanRequest historyLoanRequest);
}
