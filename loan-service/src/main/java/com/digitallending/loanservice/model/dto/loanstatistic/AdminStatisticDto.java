package com.digitallending.loanservice.model.dto.loanstatistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminStatisticDto {
    private LoanApplicationStatisticDto loanApplicationStatisticDto;
    private LoanProductStatisticDto loanProductStatisticDto;
}

