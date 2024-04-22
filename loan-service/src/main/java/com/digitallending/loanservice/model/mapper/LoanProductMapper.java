package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanproduct.LoanProductRequestDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductResponseDto;
import com.digitallending.loanservice.model.entity.LoanProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanProductMapper {
    LoanProduct requestDtoToLoanProduct(LoanProductRequestDto loanProductRequestDto);
    @Mapping(source = "loanType.loanTypeName",target = "loanTypeName")
    LoanProductResponseDto loanProductToResponseDto(LoanProduct loanProduct);
}
