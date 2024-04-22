package com.digitallending.loanservice.model.dto.loanstatistic;

import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LenderStatisticDto {
    private List<Pair<LoanRequestStatus, Long>> loanProductCountByLoanRequestStatus;
    private List<Pair<LoanTypeName, Long>> loanProductCountByLoanType;
}
