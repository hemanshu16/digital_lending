package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.model.dto.BREResultPaginationResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResultResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductPaginationResponseDto;
import com.digitallending.loanservice.model.entity.ApplicableLoanProduct;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.repository.ApplicableLoanProductRepository;
import com.digitallending.loanservice.service.def.ApplicableLoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicableLoanProductServiceImpl implements ApplicableLoanProductService {
    @Autowired
    private ApplicableLoanProductRepository applicableLoanProductRepository;

    @Override
    public void saveApplicableLoanProduct(LoanProduct loanProduct, LoanApplication loanApplication, Double interestRate, Integer score) {

        ApplicableLoanProduct applicableLoanProduct = ApplicableLoanProduct
                .builder()
                .loanProduct(loanProduct)
                .loanApplication(loanApplication)
                .score(score)
                .interestRate(interestRate)
                .build();
        applicableLoanProductRepository.save(applicableLoanProduct);
    }

    @Override
    public BREResultPaginationResponseDto getAllApplicableLoanProductByLoanApplicationId(UUID loanApplicationId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ApplicableLoanProduct> pageResponse = applicableLoanProductRepository
                .findByLoanApplicationLoanApplicationId(loanApplicationId, pageable);
        List<BREResultResponseDto> breResultResponseDtoList = new ArrayList<>();

        pageResponse
                .getContent()
                .forEach(
                        applicableLoanProduct ->
                                breResultResponseDtoList.add(
                                        BREResultResponseDto
                                                .builder()
                                                .loanProductId(applicableLoanProduct.getLoanProduct().getLoanProductId())
                                                .loanProductDescription(applicableLoanProduct.getLoanProduct().getLoanProductDescription())
                                                .loanProductName(applicableLoanProduct.getLoanProduct().getLoanProductName())
                                                .score(applicableLoanProduct.getScore())
                                                .minInterestRate(applicableLoanProduct.getInterestRate())
                                                .build()
                                )
                );
        return BREResultPaginationResponseDto
                .builder()
                .breResultResponseDtoList(breResultResponseDtoList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    public ApplicableLoanProduct findInterestRateByLoanApplicationIdAndLoanProductId(UUID loanApplicationId, UUID loanProductId) {
        return applicableLoanProductRepository
                .findByLoanApplicationLoanApplicationIdAndLoanProductLoanProductId(loanApplicationId, loanProductId);
    }

    @Override
    public void deleteApplicableLoanProductByLoanApplicationIdAndLoanProductId(UUID loanApplicationId, UUID loanProductId) {
        applicableLoanProductRepository.deleteByLoanApplicationLoanApplicationIdAndLoanProductLoanProductId(loanApplicationId, loanProductId);
    }
}
