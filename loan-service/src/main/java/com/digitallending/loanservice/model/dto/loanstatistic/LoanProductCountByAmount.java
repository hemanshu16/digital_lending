package com.digitallending.loanservice.model.dto.loanstatistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanProductCountByAmount {

    private Long loanProductAmount;
    private Integer noOfLoanProduct;
}
