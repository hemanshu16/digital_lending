package com.digitallending.loanservice.service.impl;


import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.InvalidInputException;
import com.digitallending.loanservice.exception.LoanApplicationNotFoundException;
import com.digitallending.loanservice.exception.LoanRequestNotFoundException;
import com.digitallending.loanservice.exception.SignedDocumentNotFoundException;
import com.digitallending.loanservice.model.dto.MailRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationStatusRequestDto;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.mapper.LoanApplicationMapper;
import com.digitallending.loanservice.repository.LoanApplicationRepository;
import com.digitallending.loanservice.service.def.DocumentService;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.LoanProductService;
import com.digitallending.loanservice.service.def.LoanRequestService;
import com.digitallending.loanservice.service.def.LoanTypeService;
import com.digitallending.loanservice.service.def.PropertyDetailsService;
import com.digitallending.loanservice.service.def.TransactionService;
import com.digitallending.loanservice.service.externalservicecommunication.UserServiceCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    @Lazy
    private PropertyDetailsService propertyDetailsService;
    @Autowired
    @Lazy
    private LoanRequestService loanRequestService;
    @Autowired
    @Lazy
    private LoanProductService loanProductService;
    @Autowired
    private LoanApplicationMapper loanApplicationMapper;
    @Autowired
    @Lazy
    private TransactionService transactionService;
    @Autowired
    @Lazy
    private DocumentService documentService;
    @Autowired
    private UserServiceCommunication userServiceCommunication;
    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;

    @Transactional
    @Override
    public LoanApplication getLoanApplicationById(UUID loanApplicationId) {
        return loanApplicationRepository
                .findById(loanApplicationId)
                .orElseThrow(() -> new LoanApplicationNotFoundException(
                        "loan with id :- " + loanApplicationId + " is not found!!"));
    }

    @Transactional
    @Override
    public LoanApplicationResponseDto getLoanApplicationByLoanApplicationId(UUID loanApplicationId, Role role, UUID userId) {
//                SECURITY CHECK :- THIS LOAN APPLICATION MUST BELONG TO CURRENT LOG IN USER
//                    if user is MSME then check about userId whether that loanApplication belong to the user

        LoanApplication loanApplication = getLoanApplicationById(loanApplicationId);

        List<LoanApplicationStatus> loanApplicationStatusList = Arrays
                .asList(
                        LoanApplicationStatus.IN_PROCESS,
                        LoanApplicationStatus.MODIFICATION_REQUIRED);

        if (role.equals(Role.MSME) && !loanApplication.getUserId().equals(userId)) {
//            rather than telling user that this loan application not belongs to you,
//            we will say that this loan application is not exists at all ( reason :- security )
            throw new BadRequestException("you are not owner of this loan application");
        } else if (role.equals(Role.ADMIN) &&
                loanApplicationStatusList
                        .contains(loanApplication.getLoanApplicationStatus())) {
            throw new BadRequestException("you can not view this loan application in current stage");
        }

        return loanApplicationMapper.toResponseDto(loanApplication);
    }

    @Transactional
    @Override
    public LoanApplicationResponseDto getLoanApplicationByLoanRequestId(UUID loanRequestId, UUID userId, Role role) {
//                2. CHECK in the loan request table that there is entry present for given productId and loanApplicationId
        try {
            LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);

            if (role.equals(Role.LENDER) && !loanRequest.getLoanProduct().getUserId().equals(userId)) {
                throw new BadRequestException("you are not owner of the given loan product");
            }

            return loanApplicationMapper.toResponseDto(loanRequest.getLoanApplication());
        } catch (LoanRequestNotFoundException loanRequestNotFound) {

            HistoryLoanRequest historyLoanRequest = historyLoanRequestService.getHistoryLoanRequestById(loanRequestId);

            if (role.equals(Role.LENDER) && !historyLoanRequest.getLoanProduct().getUserId().equals(userId)) {
                throw new BadRequestException("you are not owner of the given loan product");
            }

            return loanApplicationMapper.toResponseDto(historyLoanRequest.getLoanApplication());
        }
    }

    @Override
    public LoanApplicationPaginationResponseDto getFilteredLoanApplication(
            UUID userId,
            Role role,
            UUID msmeUserId,
            LoanApplicationStatus loanApplicationStatus,
            UUID loanTypeId,
            int pageNo,
            int pageSize) {

        UUID id = role.equals(Role.MSME) ? userId : msmeUserId;

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<LoanApplication> pageResponse;

        if (null != loanTypeId && null != loanApplicationStatus && id != null) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanTypeLoanTypeIdAndLoanApplicationStatusAndUserId(
                            loanTypeId, loanApplicationStatus, id, pageable);
        } else if (null != loanTypeId && null != loanApplicationStatus) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanTypeLoanTypeIdAndLoanApplicationStatus(loanTypeId, loanApplicationStatus, pageable);
        } else if (null != loanTypeId && null != id) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanTypeLoanTypeIdAndUserId(loanTypeId, id, pageable);
        } else if (null != loanApplicationStatus && null != id) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanApplicationStatusAndUserId(loanApplicationStatus, id, pageable);
        } else if (null != id) {
            pageResponse = loanApplicationRepository
                    .findAllByUserId(id, pageable);
        } else if (null != loanApplicationStatus) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanApplicationStatus(loanApplicationStatus, pageable);
        } else if (null != loanTypeId) {
            pageResponse = loanApplicationRepository
                    .findAllByLoanTypeLoanTypeId(loanTypeId, pageable);
        } else {
            pageResponse = loanApplicationRepository
                    .findAll(pageable);
        }
        List<LoanApplication> loanApplicationList = pageResponse.getContent();

        return LoanApplicationPaginationResponseDto
                .builder()
                .loanApplicationResponseDtoList(
                        loanApplicationList
                                .stream()
                                .map(loanApplication ->
                                        loanApplicationMapper.toResponseDto(loanApplication))
                                .toList())
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    private void changesForUpdate(
            LoanApplication updatedLoanApplication,
            UUID userId) {
        UUID loanApplicationId = updatedLoanApplication.getLoanApplicationId();
        LoanApplication oldLoanApplication = getLoanApplicationById(loanApplicationId);

//      checking whether the loan application is owned by user or not
        if (!oldLoanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan");
        }

        LoanApplicationStatus oldLoanApplicationStatus = oldLoanApplication.getLoanApplicationStatus();

        if ((!oldLoanApplicationStatus.equals(LoanApplicationStatus.MODIFICATION_REQUIRED) &&
                !oldLoanApplicationStatus.equals(LoanApplicationStatus.IN_PROCESS))) {
            throw new BadRequestException("you can not update loan application in this stage");
        }

        LoanType oldLoanType = oldLoanApplication.getLoanType();
        LoanType updatedLoanType = updatedLoanApplication.getLoanType();

        if (oldLoanType.getLoanTypeName().equals(LoanTypeName.PROPERTY_LOAN) &&
                !updatedLoanType.getLoanTypeName().equals(LoanTypeName.PROPERTY_LOAN)) {
            propertyDetailsService.deletePropertyDetails(loanApplicationId);
        }
        documentService.deleteExtraDocumentBasedOnUpdatedLoanType(
                loanApplicationId,
                oldLoanApplication.getLoanType(),
                updatedLoanApplication.getLoanType());

        updatedLoanApplication.setBREFilterTime(oldLoanApplication.getBREFilterTime());
        updatedLoanApplication.setLoanApplicationStatus(oldLoanApplicationStatus);
        updatedLoanApplication.setUserName(oldLoanApplication.getUserName());
    }

    @Override
    @Transactional
    public String submitLoanApplication(UUID loanApplicationId, UUID userId) {
        LoanApplication loanApplication = getLoanApplicationById(loanApplicationId);
        if (!loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan application");
        }
        List<LoanApplicationStatus> loanApplicationStatusList = Arrays
                .asList(
                        LoanApplicationStatus.IN_PROCESS,
                        LoanApplicationStatus.MODIFICATION_REQUIRED);
        if (!loanApplicationStatusList
                .contains(loanApplication.getLoanApplicationStatus()) ||
                !loanApplication.getLoanApplicationStage().equals(LoanApplicationStage.DOCUMENT_REMAINING)) {
            throw new BadRequestException("you are not allowed for verification request");
        }

        Long uploadedDocumentCount = documentService.getCountByLoanApplicationId(loanApplicationId);
        Long requiredDocumentCount = (long) loanApplication.getLoanType().getDocumentTypeList().size();

        if (0 != uploadedDocumentCount.compareTo(requiredDocumentCount)) {
            throw new BadRequestException("please provide all the documents");
        }
        loanApplicationRepository.updateLoanApplicationStageByLoanApplicationId(loanApplicationId, LoanApplicationStage.COMPLETED);
        if (loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.IN_PROCESS)) {
            loanApplicationRepository.updateLoanApplicationStatusByLoanApplicationId(loanApplicationId, LoanApplicationStatus.VERIFY);
        } else {
            // currently loan application will be in modification_required status so new status will be modified
            loanApplicationRepository.updateLoanApplicationStatusByLoanApplicationId(loanApplicationId, LoanApplicationStatus.RE_VERIFY);
        }
        return "loan application is submitted successfully";
    }

    @Override
    public void updateLoanApplicationStage(UUID loanApplicationId, LoanApplicationStage loanApplicationStage) {
//  as it is internal method so we don't require and security check
        loanApplicationRepository
                .updateLoanApplicationStageByLoanApplicationId(loanApplicationId, loanApplicationStage);
    }

    @Transactional
    @Override
    public byte[] getSignDocumentContentByLoanApplicationId(UUID loanApplicationId, UUID userId) {

        LoanApplication loanApplication = getLoanApplicationById(loanApplicationId);
        if (!loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan");
        }

        SignedDocument signedDocument = loanApplication.getSignedDocument();
        if (signedDocument == null) {
            throw new SignedDocumentNotFoundException("Sign document is not found for this loan application.");
        }
        return signedDocument.getDocumentContent();
    }

    @Override
    @Transactional
    public LoanApplicationResponseDto saveLoanApplication(
            LoanApplicationRequestDto loanApplicationRequestDto, UUID userId) {

        LoanApplication loanApplication = loanApplicationRequestDtoToLoanApplication(loanApplicationRequestDto, userId);

        if (null != loanApplicationRequestDto.getLoanApplicationId()) {
//          this is for update
            changesForUpdate(loanApplication, userId);
        } else {
            loanApplication.setUserName(userServiceCommunication.getUserName(userId));
            loanApplication.setLoanApplicationStatus(LoanApplicationStatus.IN_PROCESS);
        }
        if (loanApplication.getLoanType().getLoanTypeName().equals(LoanTypeName.PROPERTY_LOAN)) {
            loanApplication.setLoanApplicationStage(LoanApplicationStage.FIELD_REMAINING);
        } else {
            loanApplication.setLoanApplicationStage(LoanApplicationStage.DOCUMENT_REMAINING);
        }

//        save this loan application and return loanApplicationResponseDto to the user
        return loanApplicationMapper.toResponseDto(loanApplicationRepository.save(loanApplication));
    }

    @Override
    @Transactional
    public String updateLoanApplicationStatus(LoanApplicationStatusRequestDto loanApplicationStatusRequestDto) {

        UUID loanApplicationId = loanApplicationStatusRequestDto.getLoanApplicationId();
        LoanApplicationStatus newLoanApplicationStatus = loanApplicationStatusRequestDto.getLoanApplicationStatus();

        LoanApplication loanApplication = getLoanApplicationById(loanApplicationId);
        LoanApplicationStatus currentLoanApplicationStatus = loanApplication.getLoanApplicationStatus();

//      validate and update loan application status

        Map<LoanApplicationStatus, List<LoanApplicationStatus>> loanApplicationStatusMap = new EnumMap<>(LoanApplicationStatus.class);

        loanApplicationStatusMap.put(LoanApplicationStatus.VERIFIED,
                List.of(LoanApplicationStatus.VERIFY, LoanApplicationStatus.RE_VERIFY));
        loanApplicationStatusMap.put(LoanApplicationStatus.REJECTED,
                List.of(LoanApplicationStatus.VERIFY, LoanApplicationStatus.RE_VERIFY));

        loanApplicationStatusMap.put(LoanApplicationStatus.MODIFICATION_REQUIRED,
                List.of(LoanApplicationStatus.VERIFY, LoanApplicationStatus.RE_VERIFY));

        loanApplicationStatusMap.put(LoanApplicationStatus.APPROVED, List.of(LoanApplicationStatus.VERIFIED));
        loanApplicationStatusMap.put(LoanApplicationStatus.DISBURSED, List.of(LoanApplicationStatus.APPROVED));
        if (null != loanApplicationStatusMap.get(newLoanApplicationStatus) &&
                loanApplicationStatusMap.get(newLoanApplicationStatus).contains(currentLoanApplicationStatus)) {
            String modifiedMessage = "";

            if (newLoanApplicationStatus.equals(LoanApplicationStatus.MODIFICATION_REQUIRED) &&
                    (loanApplicationStatusRequestDto.getMessage().isBlank())) {
                throw new InvalidInputException("please provide message for user based on which he/she can update loan application");
            } else {
                modifiedMessage = "<p style=\"line-height: 1.5;\">loan application name :- <strong>" + loanApplication.getLoanApplicationName();
                modifiedMessage += "</strong> status changed from <strong>" + currentLoanApplicationStatus + "</strong> to <strong>" + newLoanApplicationStatus;
                modifiedMessage += "</strong>.</p><p>" + loanApplicationStatusRequestDto.getMessage() + ".</p>";
            }

            loanApplicationRepository.updateLoanApplicationStatusByLoanApplicationId(loanApplicationId, newLoanApplicationStatus);

            userServiceCommunication.sendMail(
                    MailRequestDto
                            .builder()
                            .userId(loanApplication.getUserId())
                            .subject("loan application status update")
                            .message(modifiedMessage)
                            .build());


        } else {
            throw new BadRequestException("Application is in " + currentLoanApplicationStatus + " stage");
        }

        return "Loan application status is updated";
    }

    @Override
    public void updateLoanApplicationBREFilterTime(UUID loanApplicationId, Long BREFilterTime) {
        loanApplicationRepository
                .updateBREFilterTimeByLoanApplicationId(loanApplicationId, BREFilterTime);
    }

    private LoanApplication loanApplicationRequestDtoToLoanApplication(
            LoanApplicationRequestDto loanApplicationRequestDto,
            UUID userId) {
        LoanApplication loanApplication = loanApplicationMapper.requestDtoTo(loanApplicationRequestDto);
        loanApplication.setUserId(userId);

//      storing loan type
        LoanType loanType = loanTypeService.getLoanTypeById(loanApplicationRequestDto.getLoanTypeId());
        loanApplication.setLoanType(loanType);

        return loanApplication;
    }

    @Override
    public void saveSignedDocument(LoanApplication loanApplication, SignedDocument signedDocument) {
        loanApplication.setSignedDocument(signedDocument);
        loanApplicationRepository.save(loanApplication);
    }

    @Override
    public Integer getLoanApplicationCountByLoanType(LoanTypeName loanTypeName) {
        return loanApplicationRepository
                .countByLoanTypeLoanTypeName(loanTypeName);
    }

    @Override
    public Integer getLoanApplicationByAmountRange(Long minAmount, Long maxAmount) {
        return loanApplicationRepository
                .countByAmountBetween(minAmount, maxAmount);
    }

    @Override
    public Integer getLoanApplicationCountByStatus(LoanApplicationStatus status) {
        return loanApplicationRepository
                .countByLoanApplicationStatus(status);
    }

    public void changeLoanApplicationStatusByLoanApplicationId(
            UUID loanApplicationId,
            LoanApplicationStatus loanApplicationStatus) {
        loanApplicationRepository.updateLoanApplicationStatusByLoanApplicationId(loanApplicationId, loanApplicationStatus);
    }
}
