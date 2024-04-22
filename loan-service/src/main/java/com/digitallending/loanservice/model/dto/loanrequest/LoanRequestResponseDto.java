package com.digitallending.loanservice.model.dto.loanrequest;

import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class LoanRequestResponseDto {
    private UUID loanRequestId;
    private UUID loanProductId;
    private UUID loanApplicationId;
    private Double interestRate;
    private LoanRequestStatus status;
    private UUID providerAccountId;
    private UUID receiverAccountId;
    private Integer score;
}
