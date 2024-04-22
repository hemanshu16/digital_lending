package com.digitallending.loanservice.model.dto.loanstatistic;

import com.digitallending.loanservice.model.enums.LoanTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LoanProductStatisticDto {
    private Map<LoanTypeName,Integer> loanProductCountByLoanType;
    private List<LoanProductCountByAmount> loanProductCountByAmount;
}
