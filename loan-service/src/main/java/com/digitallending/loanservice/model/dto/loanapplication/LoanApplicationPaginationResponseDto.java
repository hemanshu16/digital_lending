package com.digitallending.loanservice.model.dto.loanapplication;

import com.digitallending.loanservice.model.dto.PaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class LoanApplicationPaginationResponseDto extends PaginationResponseDto {
    private List<LoanApplicationResponseDto> loanApplicationResponseDtoList;
}
