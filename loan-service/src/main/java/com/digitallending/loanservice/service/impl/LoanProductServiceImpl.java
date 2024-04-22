package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.LoanProductNotFoundException;
import com.digitallending.loanservice.model.dto.BREResultPaginationResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BRERequestDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResult;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResultResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductRequestDto;
import com.digitallending.loanservice.model.dto.loanproduct.LoanProductResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.UserBRERequestDto;
import com.digitallending.loanservice.model.entity.ApplicableLoanProduct;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.mapper.LoanApplicationMapper;
import com.digitallending.loanservice.model.mapper.LoanProductMapper;
import com.digitallending.loanservice.repository.LoanProductRepository;
import com.digitallending.loanservice.service.def.ApplicableLoanProductService;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.LoanProductService;
import com.digitallending.loanservice.service.def.LoanRequestService;
import com.digitallending.loanservice.service.def.LoanTypeService;
import com.digitallending.loanservice.service.externalservicecommunication.BREServiceCommunication;
import com.digitallending.loanservice.service.externalservicecommunication.UserServiceCommunication;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoanProductServiceImpl implements LoanProductService {
    @Autowired
    private LoanProductRepository loanProductRepository;
    @Autowired
    private LoanProductMapper loanProductMapper;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private UserServiceCommunication userServiceCommunication;
    @Autowired
    private BREServiceCommunication breServiceCommunication;
    @Autowired
    private LoanApplicationMapper loanApplicationMapper;
    @Autowired
    private ApplicableLoanProductService applicableLoanProductService;
    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;
    @Autowired
    private LoanRequestService loanRequestService;

    @Override
    public LoanProduct getProductById(UUID productId) {
        return loanProductRepository
                .findById(productId)
                .orElseThrow(
                        () -> new LoanProductNotFoundException(
                                "loan product with id :- " + productId + " is not found"
                        )
                );
    }

    @Override
    public LoanProductResponseDto getLoanProductByProductId(UUID productId, UUID userId, Role role) {
        LoanProduct loanProduct = getProductById(productId);
        if (role.equals(Role.LENDER) && !loanProduct.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan product");
        }
        return loanProductMapper.loanProductToResponseDto(loanProduct);
    }

    @Override
    public LoanProductPaginationResponseDto getFilteredLoanProduct(
            UUID userId, Role role, UUID lenderId, UUID loanTypeId, int pageNo, int pageSize) {

        UUID id = role.equals(Role.LENDER) ? userId : lenderId;

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<LoanProduct> pageResponse;

        if (null != loanTypeId && null != id) {
            pageResponse = loanProductRepository.findAllByUserIdAndLoanTypeLoanTypeId(id, loanTypeId, pageable);
        } else if (null != id) {
            pageResponse = loanProductRepository.findAllByUserId(id, pageable);
        } else if (null != loanTypeId) {
            pageResponse = loanProductRepository.findAllByLoanTypeLoanTypeId(loanTypeId, pageable);
        } else {
            pageResponse = loanProductRepository.findAll(pageable);
        }

        List<LoanProduct> loanProductList = pageResponse.getContent();
        return LoanProductPaginationResponseDto
                .builder()
                .loanProductResponseDtoList(
                        loanProductList
                                .stream()
                                .map(
                                        loanProduct ->
                                                loanProductMapper
                                                        .loanProductToResponseDto(loanProduct)
                                ).toList()
                )
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Transactional
    @Override
    public BREResultPaginationResponseDto showApplicableLoanProductByApplicationId(UUID loanApplicationId, int pageNo, int pageSize) {

        if (Boolean.TRUE.equals(historyLoanRequestService
                .isRequestExistsByLoanApplicationId(loanApplicationId))) {
            throw new BadRequestException("You can not see loan products after accepted loan request from another loan product.");
        }

        updateApplicableLoanProductByLoanApplicationId(loanApplicationId);

        return applicableLoanProductService
                .getAllApplicableLoanProductByLoanApplicationId(loanApplicationId,pageNo,pageSize);
    }

    @Override
    public UUID getLoanTypeIdByLoanProductId(UUID loanProductId) {
        return loanProductRepository
                .findByLoanProductId(loanProductId)
                .orElseThrow(() -> new LoanProductNotFoundException("loan product with id " + loanProductId + " is not found"))
                .getLoanType()
                .getLoanTypeId();
    }

    private void updateApplicableLoanProductByLoanApplicationId(UUID loanApplicationId) {
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);

        if (!loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.VERIFIED)) {
            throw new BadRequestException("loan application status is not verified");
        }
        loanApplicationService
                .updateLoanApplicationBREFilterTime(loanApplicationId, Instant.now().getEpochSecond());

        List<LoanProduct> potentialBRELoanProducts = applyBasicFilter(loanApplication);

        if (potentialBRELoanProducts.isEmpty()) {
            return;
        }

        Map<UUID, LoanProduct> potentialBRELoanProductsMap = potentialBRELoanProducts.stream()
                .collect(Collectors.toMap(LoanProduct::getLoanProductId, loanProduct -> loanProduct));

        List<BREResult> breResultList = applyBreFilterOnGivenLoanProducts(potentialBRELoanProducts, loanApplication);

//      save this bre result to applicableLoanProduct table
        breResultList
                .forEach(
                        breResult -> {
                            LoanProduct loanProduct = potentialBRELoanProductsMap.get(breResult.getLoanProductId());
                            applicableLoanProductService.saveApplicableLoanProduct(
                                    loanProduct, loanApplication, breResult.getMinInterestRate(), breResult.getScore());
                        }
                );
    }

    private List<LoanProduct> applyBasicFilter(LoanApplication loanApplication) {

        List<LoanProduct> loanProductList = getAllLoanProductByCreationTimeAndLoanTypeId(
                loanApplication.getBREFilterTime(), loanApplication.getLoanType().getLoanTypeId());

        return loanProductList
                .stream()
                .filter(
                        loanProduct -> loanProduct.getMaxAmount() >= loanApplication.getAmount() &&
                                loanProduct.getMaxTenure() >= loanApplication.getTenure()
                ).toList();
    }

    private List<LoanProduct> getAllLoanProductByCreationTimeAndLoanTypeId(Long breFilterTime, UUID loanTypeId) {
        return loanProductRepository
                .findByCreationTimeGreaterThanEqualAndLoanTypeLoanTypeId(breFilterTime, loanTypeId);
    }

    private List<BREResult> applyBreFilterOnGivenLoanProducts(
            List<LoanProduct> potentialBRELoanProducts, LoanApplication loanApplication) {
        UserBRERequestDto userBRERequestDto = userServiceCommunication.getBRERequestUserDto(loanApplication.getUserId());

        List<UUID> potentialBRELoanProductIdList = new ArrayList<>();

        potentialBRELoanProducts
                .forEach(
                        potentialBRELoanProduct -> potentialBRELoanProductIdList.add(potentialBRELoanProduct.getLoanProductId())
                );

        BRERequestDto breRequestDto = BRERequestDto
                .builder()
                .userRequestDTO(userBRERequestDto)
                .loanApplicationRequestDTO(loanApplicationMapper.loanApplicationToLoanApplicationBRERequestDto(loanApplication))
                .loanProductIdList(potentialBRELoanProductIdList)
                .build();

        return breServiceCommunication.getBREResult(breRequestDto);
    }

    @Override
    public LoanProductResponseDto saveLoanProduct(LoanProductRequestDto loanProductRequestDto) {

        userServiceCommunication.isUserExists(loanProductRequestDto.getUserId(), Role.LENDER);

        LoanProduct loanProduct = loanProductMapper.requestDtoToLoanProduct(loanProductRequestDto);
        loanProduct.setUserName(userServiceCommunication.getUserName(loanProductRequestDto.getUserId()));

        LoanType loanType = loanTypeService.getLoanTypeById(loanProductRequestDto.getLoanTypeId());
        loanProduct.setLoanType(loanType);
        return loanProductMapper.loanProductToResponseDto(loanProductRepository.save(loanProduct));
    }

    @Override
    public Integer getLoanProductCountByLoanType(LoanTypeName loanTypeName) {
        return loanProductRepository
                .countByLoanTypeLoanTypeName(loanTypeName);
    }

    @Override
    public Integer getLoanProductCountByAmountRange(Long minAmount, Long maxAmount) {
        return loanProductRepository
                .countByMaxAmountBetween(minAmount, maxAmount);
    }

    @Override
    public Long getLoanProductCountByLoanTypeAndUserId(LoanTypeName loanTypeName, UUID userId) {
        return loanProductRepository
                .countByLoanTypeLoanTypeNameAndUserId(loanTypeName, userId);
    }
}