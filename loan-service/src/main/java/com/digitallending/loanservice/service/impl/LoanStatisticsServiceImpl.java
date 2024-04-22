package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.model.dto.LoanTypeResponseDto;
import com.digitallending.loanservice.model.dto.loanstatistic.LenderStatisticDto;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanApplicationCountByAmount;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanApplicationStatisticDto;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanProductCount;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanProductCountByAmount;
import com.digitallending.loanservice.model.dto.loanstatistic.LoanProductStatisticDto;
import com.digitallending.loanservice.model.dto.loanstatistic.AdminStatisticDto;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.LoanProductService;
import com.digitallending.loanservice.service.def.LoanRequestService;
import com.digitallending.loanservice.service.def.LoanStatisticService;
import com.digitallending.loanservice.service.def.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanStatisticsServiceImpl implements LoanStatisticService {
    private final List<Long> amountList = List.of(0L, 3_00_000L, 6_00_000L, 9_00_000L, 12_00_000L, 15_00_000L, Long.MAX_VALUE);
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LoanProductService loanProductService;
    @Autowired
    private LoanTypeService loanTypeService;

    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;
    @Autowired
    private LoanRequestService loanRequestService;

    @Override
    public AdminStatisticDto getLoanStatisticForAdmin() {

        EnumMap<LoanTypeName, Integer> loanApplicationCountByLoanType = new EnumMap<>(LoanTypeName.class);
        EnumMap<LoanTypeName, Integer> loanProductCountByLoanType = new EnumMap<>(LoanTypeName.class);

        List<LoanProductCountByAmount> loanProductCountByAmount = new ArrayList<>();
        List<LoanApplicationCountByAmount> loanApplicationCountByAmount = new ArrayList<>();

        EnumMap<LoanApplicationStatus, Integer> loanApplicationCountByStatus = new EnumMap<>(LoanApplicationStatus.class);

        List<LoanTypeResponseDto> loanTypeList = loanTypeService.getAllLoanType();

        loanTypeList
                .forEach(
                        loanType -> {
                            loanProductCountByLoanType
                                    .put(loanType.getLoanTypeName(),
                                            loanProductService
                                                    .getLoanProductCountByLoanType(loanType.getLoanTypeName()));
                            loanApplicationCountByLoanType
                                    .put(loanType.getLoanTypeName(),
                                            loanApplicationService
                                                    .getLoanApplicationCountByLoanType(loanType.getLoanTypeName()));
                        }
                );

        for (int i = 1; i < amountList.size(); i++) {

            loanProductCountByAmount
                    .add(LoanProductCountByAmount.builder()
                            .loanProductAmount(amountList.get(i))
                            .noOfLoanProduct(loanProductService
                                    .getLoanProductCountByAmountRange(amountList.get(i - 1), amountList.get(i)))
                            .build());
            loanApplicationCountByAmount
                    .add(LoanApplicationCountByAmount
                            .builder()
                            .loanAmount(amountList.get(i))
                            .noOfLoanApplication(loanApplicationService
                                    .getLoanApplicationByAmountRange(amountList.get(i - 1), amountList.get(i)))
                            .build());
        }

        for (LoanApplicationStatus status : LoanApplicationStatus.values()) {
            loanApplicationCountByStatus
                    .put(status,
                            loanApplicationService
                                    .getLoanApplicationCountByStatus(status));
        }
        LoanApplicationStatisticDto loanApplicationStatisticDto = LoanApplicationStatisticDto
                .builder()
                .loanApplicationCountByAmount(loanApplicationCountByAmount)
                .loanApplicationCountByLoanType(loanApplicationCountByLoanType)
                .loanApplicationCountByStatus(loanApplicationCountByStatus)
                .build();
        LoanProductStatisticDto loanProductStatisticDto = LoanProductStatisticDto
                .builder()
                .loanProductCountByAmount(loanProductCountByAmount)
                .loanProductCountByLoanType(loanProductCountByLoanType)
                .build();

        return AdminStatisticDto
                .builder()
                .loanApplicationStatisticDto(loanApplicationStatisticDto)
                .loanProductStatisticDto(loanProductStatisticDto)
                .build();
    }

    @Override
    public LenderStatisticDto getLoanStatisticForLender(UUID userId) {

        List<Pair<LoanRequestStatus, Long>> loanProductCountByLoanRequestStatus = new ArrayList<>();

        List<Pair<LoanTypeName, Long>> loanProductCountByLoanType = new ArrayList<>();

        for (LoanRequestStatus loanRequestStatus : LoanRequestStatus.values()) {
            Long loanProductCount;

            if (loanRequestStatus.equals(LoanRequestStatus.REJECTED_BY_LENDER) || loanRequestStatus.equals(LoanRequestStatus.REJECTED_BY_MSME)) {
                loanProductCount = loanRequestService
                        .getLoanProductCountByUserIdAndLoanRequestStatus(userId, loanRequestStatus) +
                        historyLoanRequestService.getLoanProductCountByUserIdAndLoanRequestStatus(userId,loanRequestStatus);
            } else {
                if (loanRequestStatus.equals(LoanRequestStatus.ACCEPTED)) {
                    loanProductCount = historyLoanRequestService
                            .getLoanProductCountByUserIdAndLoanRequestStatus(userId, loanRequestStatus);
                } else {
                    loanProductCount = loanRequestService
                            .getLoanProductCountByUserIdAndLoanRequestStatus(userId, loanRequestStatus);
                }
            }
            loanProductCountByLoanRequestStatus.add(Pair.of(loanRequestStatus, loanProductCount));
        }

        List<LoanTypeResponseDto> loanTypeList = loanTypeService.getAllLoanType();

        loanTypeList
                .forEach(
                        loanType ->
                                loanProductCountByLoanType
                                        .add(Pair.of(loanType.getLoanTypeName(),
                                                loanProductService
                                                        .getLoanProductCountByLoanTypeAndUserId(loanType.getLoanTypeName(), userId))
                                        )
                );

        return LenderStatisticDto
                .builder()
                .loanProductCountByLoanType(loanProductCountByLoanType)
                .loanProductCountByLoanRequestStatus(loanProductCountByLoanRequestStatus)
                .build();
    }
}
