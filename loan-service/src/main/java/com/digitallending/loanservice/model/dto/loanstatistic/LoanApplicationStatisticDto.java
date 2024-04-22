package com.digitallending.loanservice.model.dto.loanstatistic;

import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@Builder
public class LoanApplicationStatisticDto {
    Map<LoanTypeName,Integer> loanApplicationCountByLoanType;
    List<LoanApplicationCountByAmount> loanApplicationCountByAmount;
    Map<LoanApplicationStatus,Integer> loanApplicationCountByStatus;
}
