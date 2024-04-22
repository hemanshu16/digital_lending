package com.digitallending.loanservice.service.def;


import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;

import java.util.List;
import java.util.UUID;

public interface HistoryLoanRequestService {

    Boolean isRequestExistsByLoanApplicationId(UUID loanApplicationId);

    void saveLoanAllRequestForHistory(List<LoanRequest> loanRequestList);

    Boolean existByLoanApplicationLoanApplicationIdAndStatus(
            UUID loanApplicationId,
            LoanRequestStatus loanRequestStatus);

    List<HistoryLoanRequest> findByLoanApplicationLoanApplicationIdAndStatus(UUID loanApplicationId, LoanRequestStatus status);

    List<HistoryLoanRequest> findByLoanProductLoanProductIdAndStatus(UUID loanProductId, LoanRequestStatus status);

    HistoryLoanRequest getHistoryLoanRequestById(UUID historyLoanRequestId);

    void changeProviderAccountId(UUID historyLoanRequestId, UUID providerAccountId);

    Long getLoanProductCountByUserIdAndLoanRequestStatus(UUID userId, LoanRequestStatus loanRequestStatus);

    HistoryLoanRequest save(HistoryLoanRequest historyLoanRequest);
}
