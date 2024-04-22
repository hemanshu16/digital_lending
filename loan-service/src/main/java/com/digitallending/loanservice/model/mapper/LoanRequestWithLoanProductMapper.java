package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LoanRequestMapper.class)
public interface LoanRequestWithLoanProductMapper {

    LoanRequestResponseWithLoanProductDto toLoanRequestResponseWithLoanProductDto(
            LoanRequestResponseDto loanRequest,
            String lenderUserName,
            String loanProductName);
}
