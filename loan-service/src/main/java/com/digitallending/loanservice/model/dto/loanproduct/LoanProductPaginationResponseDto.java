package com.digitallending.loanservice.model.dto.loanproduct;

import com.digitallending.loanservice.model.dto.PaginationResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class LoanProductPaginationResponseDto extends PaginationResponseDto {
    private List<LoanProductResponseDto> loanProductResponseDtoList;
}
