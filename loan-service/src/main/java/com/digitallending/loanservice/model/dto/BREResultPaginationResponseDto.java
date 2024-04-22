package com.digitallending.loanservice.model.dto;

import com.digitallending.loanservice.model.dto.externalservice.bre.BREResultResponseDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class BREResultPaginationResponseDto extends PaginationResponseDto{
    List<BREResultResponseDto> breResultResponseDtoList;
}
