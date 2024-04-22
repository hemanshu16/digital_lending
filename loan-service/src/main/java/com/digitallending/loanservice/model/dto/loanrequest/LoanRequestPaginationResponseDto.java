package com.digitallending.loanservice.model.dto.loanrequest;

import com.digitallending.loanservice.model.dto.PaginationResponseDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class LoanRequestPaginationResponseDto extends PaginationResponseDto {
    List<LoanRequestResponseWithLoanApplicationDto> loanRequestResponseWithLoanApplicationDtoList;
}
