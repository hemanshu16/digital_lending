package com.digitallending.loanservice.model.dto.loanrequest;

import com.digitallending.loanservice.model.enums.LoanApplicationProcessStage;
import lombok.Data;

@Data
public class LoanRequestResponseWithLoanApplicationDto extends LoanRequestResponseDto {
    private String MSMEUserName;
    private String loanApplicationName;
    private LoanApplicationProcessStage loanApplicationProcessStage;
    private Long amount;
    private Long tenure;
}
