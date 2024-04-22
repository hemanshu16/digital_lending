package com.digitallending.loanservice.model.dto.loanstatistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanApplicationCountByAmount {
    private Long loanAmount;
    private Integer noOfLoanApplication;
}
