package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanstatistic.AdminStatisticDto;
import com.digitallending.loanservice.model.dto.loanstatistic.LenderStatisticDto;

import java.util.UUID;

public interface LoanStatisticService {
    AdminStatisticDto getLoanStatisticForAdmin();

    LenderStatisticDto getLoanStatisticForLender(UUID userId);
}
