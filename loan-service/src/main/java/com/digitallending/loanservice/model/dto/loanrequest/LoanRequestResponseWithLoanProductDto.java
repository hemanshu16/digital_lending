package com.digitallending.loanservice.model.dto.loanrequest;

import lombok.Data;

@Data
public class LoanRequestResponseWithLoanProductDto extends LoanRequestResponseDto {
    private String lenderUserName;
    private String loanProductName;
}
