package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mapping(source = "loanProduct.loanProductId", target = "loanProductId")
    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    LoanRequestResponseDto toDto(LoanRequest loanRequest);
    @Mapping(source = "loanProduct.loanProductId", target = "loanProductId")
    @Mapping(source = "loanApplication.loanApplicationId", target = "loanApplicationId")
    LoanRequestResponseDto historyToDto(HistoryLoanRequest historyLoanRequest);
}
