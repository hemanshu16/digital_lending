package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.LoanRequestNotFoundException;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanProductCount;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.mapper.HistoryLoanRequestMapper;

import com.digitallending.loanservice.repository.HistoryLoanRequestRepository;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class HistoryLoanRequestServiceImpl implements HistoryLoanRequestService {

    @Autowired
    private HistoryLoanRequestRepository historyLoanRequestRepository;

    @Autowired
    private HistoryLoanRequestMapper historyLoanRequestMapper;

    @Override
    public Boolean isRequestExistsByLoanApplicationId(UUID loanApplicationId) {

        return historyLoanRequestRepository
                .existsByLoanApplicationLoanApplicationId(loanApplicationId);
    }

    @Override
    public HistoryLoanRequest getHistoryLoanRequestById(UUID historyLoanRequestId) {
        return historyLoanRequestRepository
                .findById(historyLoanRequestId)
                .orElseThrow(() ->
                        new LoanRequestNotFoundException("Loan request with given id:" +
                                historyLoanRequestId +
                                " is not found."));
    }

    @Override
    public Long getLoanProductCountByUserIdAndLoanRequestStatus(UUID userId, LoanRequestStatus loanRequestStatus) {
        return historyLoanRequestRepository
                .countByLoanProductUserIdAndStatus(userId, loanRequestStatus);
    }

    @Override
    public HistoryLoanRequest save(HistoryLoanRequest historyLoanRequest) {
        return historyLoanRequestRepository.save(historyLoanRequest);
    }

    @Override
    public void saveLoanAllRequestForHistory(List<LoanRequest> loanRequestList) {

        List<HistoryLoanRequest> historyLoanRequestList = loanRequestList.stream().map(
                loanRequest ->
                        HistoryLoanRequest
                                .builder()
                                .loanRequestId(loanRequest.getLoanRequestId())
                                .loanRequestId(loanRequest.getLoanRequestId())
                                .loanProduct(loanRequest.getLoanProduct())
                                .loanApplication(loanRequest.getLoanApplication())
                                .interestRate(loanRequest.getInterestRate())
                                .status(loanRequest.getStatus())
                                .score(loanRequest.getScore())
                                .build()
        ).toList();
        historyLoanRequestRepository.saveAll(historyLoanRequestList);
    }

    @Override
    public Boolean existByLoanApplicationLoanApplicationIdAndStatus(UUID loanApplicationId, LoanRequestStatus loanRequestStatus) {
        return historyLoanRequestRepository
                .existsByLoanApplicationLoanApplicationIdAndStatus(loanApplicationId, loanRequestStatus);
    }

    @Override
    public List<HistoryLoanRequest> findByLoanApplicationLoanApplicationIdAndStatus(
            UUID loanApplicationId, LoanRequestStatus status) {
        return historyLoanRequestRepository.findByLoanApplicationLoanApplicationIdAndStatus(loanApplicationId, status);
    }

    @Override
    public List<HistoryLoanRequest> findByLoanProductLoanProductIdAndStatus(
            UUID loanProductId, LoanRequestStatus status) {
        return historyLoanRequestRepository.findByLoanProductLoanProductIdAndStatus(loanProductId,status);
    }
    @Override
    public void changeProviderAccountId(UUID historyLoanRequestId, UUID providerAccountId) {
        historyLoanRequestRepository
                .updateProviderAccountId(historyLoanRequestId, providerAccountId);
    }
}
