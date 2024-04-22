package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.LoanRequestNotFoundException;
import com.digitallending.loanservice.model.dto.MailRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestAcceptRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestOfferDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestRequestDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanApplicationDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanProductDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestStatusDto;
import com.digitallending.loanservice.model.entity.ApplicableLoanProduct;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.model.enums.LoanApplicationProcessStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import com.digitallending.loanservice.model.mapper.HistoryLoanRequestMapper;
import com.digitallending.loanservice.model.mapper.LoanRequestMapper;
import com.digitallending.loanservice.model.mapper.LoanRequestWithLoanApplicationMapper;
import com.digitallending.loanservice.model.mapper.LoanRequestWithLoanProductMapper;
import com.digitallending.loanservice.model.mapper.SignedDocumentMapper;
import com.digitallending.loanservice.repository.HistoryLoanRequestRepository;
import com.digitallending.loanservice.repository.LoanRequestRepository;
import com.digitallending.loanservice.service.def.ApplicableLoanProductService;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.LoanProductService;
import com.digitallending.loanservice.service.def.LoanRequestService;
import com.digitallending.loanservice.service.def.SignedDocumentService;
import com.digitallending.loanservice.service.externalservicecommunication.UserServiceCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    @Lazy
    private LoanApplicationService loanApplicationService;

    @Autowired
    @Lazy
    private LoanProductService loanProductService;

    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;

    @Autowired
    private LoanRequestMapper loanRequestMapper;

    @Autowired
    private HistoryLoanRequestMapper historyLoanRequestMapper;

    @Autowired
    private HistoryLoanRequestRepository historyLoanRequestRepository;

    @Autowired
    private SignedDocumentService signedDocumentService;

    @Autowired
    private SignedDocumentMapper signedDocumentMapper;

    @Autowired
    private ApplicableLoanProductService applicableLoanProductService;

    @Autowired
    private UserServiceCommunication userServiceCommunication;

    @Autowired
    private LoanRequestWithLoanProductMapper loanRequestWithLoanProductMapper;

    @Autowired
    private LoanRequestWithLoanApplicationMapper loanRequestWithLoanApplicationMapper;

    @Override
    public LoanRequest getLoanRequestById(UUID loanRequestId) {
        return loanRequestRepository
                .findById(loanRequestId)
                .orElseThrow(() ->
                        new LoanRequestNotFoundException("Loan request with id :" + loanRequestId + " not found."));

    }

    @Override
    public LoanRequestResponseDto getLoanRequestResponseDtoById(UUID loanRequestId) {
        try {
            return loanRequestMapper.toDto(getLoanRequestById(loanRequestId));
        } catch (LoanRequestNotFoundException loanRequestNotFoundException) {
            HistoryLoanRequest historyLoanRequest = historyLoanRequestService.getHistoryLoanRequestById(loanRequestId);
            return loanRequestMapper.historyToDto(historyLoanRequest);
        }
    }


    @Override
    public List<LoanRequestResponseWithLoanProductDto> getAllLoanRequestByLoanApplicationIdAndStatus(
            UUID loanApplicationId, String role, UUID userId, LoanRequestStatus status) {

        if (Role.valueOf(role).equals(Role.MSME)) {

            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
            if (!loanApplication.getUserId().equals(userId)) {
                throw new BadRequestException("Give loan application :" + loanApplicationId + " is not owned by user :" + userId);
            }
        }

        List<LoanRequestResponseWithLoanProductDto> loanRequestResponseWithLoanProductDtoList = loanRequestRepository
                .findByLoanApplicationLoanApplicationIdAndStatus(loanApplicationId, status)
                .stream()
                .map(loanRequest -> {
                    LoanRequestResponseDto loanRequestResponseDto = loanRequestMapper.toDto(loanRequest);
                    String lenderUserName = loanRequest.getLoanProduct().getUserName();
                    return loanRequestWithLoanProductMapper.toLoanRequestResponseWithLoanProductDto(
                            loanRequestResponseDto,
                            lenderUserName,
                            loanRequest.getLoanProduct().getLoanProductName());
                })
                .toList();

        if (loanRequestResponseWithLoanProductDtoList.isEmpty()) {
            loanRequestResponseWithLoanProductDtoList = historyLoanRequestService
                    .findByLoanApplicationLoanApplicationIdAndStatus(loanApplicationId, status)
                    .stream()
                    .map(historyLoanRequest -> {
                        LoanRequestResponseDto loanRequestResponseDto = historyLoanRequestMapper.toDto(historyLoanRequest);
                        String lenderUserName = historyLoanRequest.getLoanProduct().getUserName();
                        return loanRequestWithLoanProductMapper.toLoanRequestResponseWithLoanProductDto(
                                loanRequestResponseDto,
                                lenderUserName,
                                historyLoanRequest.getLoanProduct().getLoanProductName());
                    })
                    .toList();
        }

        return loanRequestResponseWithLoanProductDtoList;
    }

    @Transactional
    @Override
    public LoanRequestPaginationResponseDto getAllLoanRequestByLoanProductIdAndStatus(
            UUID loanProductId, String role, UUID userId, LoanRequestStatus status, int pageSize, int pageNumber) {

        if (Role.valueOf(role).equals(Role.LENDER)) {

            LoanProduct loanProduct = loanProductService.getProductById(loanProductId);
            if (!loanProduct.getUserId().equals(userId)) {
                throw new BadRequestException("Give loan product :" + loanProductId + " is not owned by user :" + userId);
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<LoanRequestResponseDto> pageResponse = loanRequestRepository
                .findAllByLoanProductLoanProductIdAndStatus(loanProductId, status, pageable);

        List<LoanRequestResponseWithLoanApplicationDto> loanRequestResponseWithLoanApplicationDtoList =
                pageResponse
                        .getContent()
                        .stream()
                        .map(loanRequestResponseDto -> {
                            LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(
                                    loanRequestResponseDto.getLoanApplicationId());
                            String MSMEUserName = loanApplication.getUserName();

                            boolean flag = loanRequestResponseDto.getStatus().equals(LoanRequestStatus.ACCEPTED) && loanApplication.getSignedDocument() != null;

                            LoanApplicationProcessStage loanApplicationProcessStage;

                            if (loanRequestResponseDto.getStatus().equals(LoanRequestStatus.ACCEPTED)) {

                                if (loanApplication.getSignedDocument().getStatus().equals(SignedDocumentStatus.UNSIGNED) ||
                                        loanApplication.getSignedDocument().getStatus().equals(SignedDocumentStatus.RESIGNED)) {

                                    loanApplicationProcessStage = LoanApplicationProcessStage.AGREEMENT_NEEDS_TO_SIGN_BY_MSME;
                                } else if (loanApplication.getSignedDocument().getStatus().equals(SignedDocumentStatus.SIGNED_BY_MSME)) {

                                    loanApplicationProcessStage = LoanApplicationProcessStage.PLEASE_SIGN_AGREEMENT;
                                } else if (loanApplication.getSignedDocument().getStatus().equals(SignedDocumentStatus.SIGNED_BY_MSME_AND_LENDER)) {

                                    loanApplicationProcessStage = LoanApplicationProcessStage.IN_VERIFICATION_PROCESS;
                                } else if (loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.APPROVED)) {
//                                    loanApplication.getSignedDocument().getStatus().equals(SignedDocumentStatus.APPROVED)
                                    loanApplicationProcessStage = LoanApplicationProcessStage.PLEASE_MAKE_PAYMENT;
                                } else {
                                    loanApplicationProcessStage = LoanApplicationProcessStage.NOT_APPLICABLE;
                                }
                            } else {
                                loanApplicationProcessStage = LoanApplicationProcessStage.NOT_APPLICABLE;
                            }

                            return loanRequestWithLoanApplicationMapper.toLoanRequestResponseWithLoanApplicationDto(
                                    loanRequestResponseDto,
                                    loanApplicationProcessStage,
                                    loanApplication.getLoanApplicationName(),
                                    MSMEUserName,
                                    loanApplication.getAmount(),
                                    loanApplication.getTenure());
                        })
                        .toList();

        return LoanRequestPaginationResponseDto
                .builder()
                .loanRequestResponseWithLoanApplicationDtoList(loanRequestResponseWithLoanApplicationDtoList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Transactional
    @Override
    public LoanRequestResponseDto saveLoanRequest(LoanRequestRequestDto loanRequestRequestDto, UUID userId) {

        if (loanRequestRepository.existsByLoanApplicationLoanApplicationIdAndStatusIn(
                loanRequestRequestDto.getLoanApplicationId(),
                Arrays.asList(LoanRequestStatus.REQUESTED, LoanRequestStatus.OFFERED)) ||
                Boolean.TRUE.equals(historyLoanRequestService.existByLoanApplicationLoanApplicationIdAndStatus(
                        loanRequestRequestDto.getLoanApplicationId(),
                        LoanRequestStatus.ACCEPTED))) {
            throw new BadRequestException("You have already made request to another loan product.");
        }

        LoanApplication loanApplication = loanApplicationService
                .getLoanApplicationById(loanRequestRequestDto.getLoanApplicationId());

//      given loan app. should be created by logged user
        if (!loanApplication.getUserId().equals(userId)) {

            throw new BadRequestException("Given loan application is not owned by user :" + userId);

        }

        LoanProduct loanProduct = loanProductService.getProductById(loanRequestRequestDto.getLoanProductId());

        ApplicableLoanProduct applicableLoanProduct = applicableLoanProductService
                .findInterestRateByLoanApplicationIdAndLoanProductId(
                        loanRequestRequestDto.getLoanApplicationId(),
                        loanRequestRequestDto.getLoanProductId());

        if (applicableLoanProduct == null) {
            throw new BadRequestException("Your application is not satisfying constraint specified by given loan product");
        }

        applicableLoanProductService.deleteApplicableLoanProductByLoanApplicationIdAndLoanProductId(
                loanRequestRequestDto.getLoanApplicationId(),
                loanRequestRequestDto.getLoanProductId());

        LoanRequest loanRequest = LoanRequest
                .builder()
                .loanApplication(loanApplication)
                .loanProduct(loanProduct)
                .interestRate(applicableLoanProduct.getInterestRate())
                .score(applicableLoanProduct.getScore())
                .status(LoanRequestStatus.REQUESTED)
                .build();
        loanRequest = loanRequestRepository.save(loanRequest);
        String lenderUserName = loanProduct.getUserName();

        userServiceCommunication.sendMail(
                MailRequestDto
                        .builder()
                        .userId(loanRequest.getLoanProduct().getUserId())
                        .subject("Request received")
                        .message(
                                "<p style=\"line-height: 1.5;\">You received request from the loan application <strong>" +
                                        loanApplication.getLoanApplicationName() +
                                        "</strong> for the loan product <strong>"
                                        + loanProduct.getLoanProductName() + "</strong>.</p>")
                        .build()
        );

        userServiceCommunication.sendMail(
                MailRequestDto
                        .builder()
                        .userId(loanRequest.getLoanProduct().getUserId())
                        .subject("Request received")
                        .message(
                                "<p style=\"line-height: 1.5;\">You received request from the loan application <strong>" +
                                        loanApplication.getLoanApplicationName() +
                                        "</strong> for the loan product <strong>"
                                        + loanProduct.getLoanProductName() + "</strong>.</p>")
                        .build()
        );

        return loanRequestWithLoanProductMapper.toLoanRequestResponseWithLoanProductDto(
                loanRequestMapper.toDto(loanRequest),
                lenderUserName,
                loanProduct.getLoanProductName());
    }


    @Override
    @Transactional
    public LoanRequestResponseDto changeLoanRequestInterestRateAndStatus(LoanRequestOfferDto loanRequestOfferDto, UUID userId) {

        UUID loanRequestId = loanRequestOfferDto.getLoanRequestId();
        LoanRequest loanRequest = getLoanRequestById(loanRequestId);

        if (!loanRequest.getStatus().equals(LoanRequestStatus.REQUESTED)) {
            throw new BadRequestException("Loan request should be in requested state.");
        }
        LoanProduct loanProduct = loanRequest.getLoanProduct();
        if (!loanProduct.getUserId().equals(userId)) {
            throw new BadRequestException("Loan product with id :" + loanProduct.getLoanProductId() + " is not owned by user :" + userId);
        }

        loanRequest.setInterestRate(loanRequestOfferDto.getInterestRate());
        loanRequest.setStatus(LoanRequestStatus.OFFERED);
        loanRequestRepository.save(loanRequest);

        String MSMEUserName = loanRequest.getLoanApplication().getUserName();

        userServiceCommunication.sendMail(
                MailRequestDto
                        .builder()
                        .userId(loanRequest.getLoanApplication().getUserId())
                        .subject("Offer received")
                        .message("<p style=\"line-height: 1.5;\">You received an offer from loan product <strong>" +
                                loanProduct.getLoanProductName() +
                                "</strong> for the loan application <strong>" +
                                loanRequest.getLoanApplication().getLoanApplicationName() + "</strong>.</p>")
                        .build()
        );

        return loanRequestWithLoanApplicationMapper.toLoanRequestResponseWithLoanApplicationDto(
                loanRequestMapper.toDto(loanRequest),
                LoanApplicationProcessStage.NOT_APPLICABLE,
                loanRequest.getLoanApplication().getLoanApplicationName(),
                MSMEUserName,
                loanRequest.getLoanApplication().getAmount(),
                loanRequest.getLoanApplication().getTenure());
    }

    @Transactional
    @Override
    public LoanRequestResponseDto changeLoanRequestStatus(LoanRequestStatusDto loanRequestStatusDto, String role, UUID userId) {

        LoanRequest loanRequest = getLoanRequestById(loanRequestStatusDto.getLoanRequestId());

        if (Role.valueOf(role).equals(Role.LENDER)) {

            LoanProduct loanProduct = loanRequest.getLoanProduct();
            if (!loanRequest.getStatus().equals(LoanRequestStatus.REQUESTED)) {
                throw new BadRequestException("Loan request should be in requested state.");
            }
            if (!loanProduct.getUserId().equals(userId)) {
                throw new BadRequestException("Loan product with id :" + loanProduct.getLoanProductId() + " is not owned by user :" + userId);
            }

            loanRequest.setStatus(LoanRequestStatus.REJECTED_BY_LENDER);
            loanRequestRepository.save(loanRequest);
            String MSMEUserName = loanRequest.getLoanApplication().getUserName();

            userServiceCommunication
                    .sendMail(
                            MailRequestDto
                                    .builder()
                                    .subject("Request rejection")
                                    .userId(loanRequest.getLoanApplication().getUserId())
                                    .message(
                                            "<p style=\"line-height: 1.5;\">Your request is rejected to the loan product <strong>" +
                                                    loanProduct.getLoanProductName() +
                                                    "</strong> for the loan application <strong>" +
                                                    loanRequest.getLoanApplication().getLoanApplicationName() + "</strong>.</p>")
                                    .build()
                    );
            return loanRequestWithLoanApplicationMapper.toLoanRequestResponseWithLoanApplicationDto(
                    loanRequestMapper.toDto(loanRequest),
                    LoanApplicationProcessStage.NOT_APPLICABLE,
                    loanRequest.getLoanApplication().getLoanApplicationName(),
                    MSMEUserName,
                    loanRequest.getLoanApplication().getAmount(),
                    loanRequest.getLoanApplication().getTenure());


        } else {
            LoanApplication loanApplication = loanRequest.getLoanApplication();
            if (loanRequest.getStatus() != LoanRequestStatus.OFFERED) {
                throw new BadRequestException("Loan request should be in offered state.");
            }

            if (!loanApplication.getUserId().equals(userId)) {
                throw new BadRequestException("Loan application with id :" + loanApplication.getLoanApplicationId() + " is not owned by user :" + userId);
            }

            loanRequest.setStatus(LoanRequestStatus.REJECTED_BY_MSME);
            loanRequestRepository.save(loanRequest);
            String lenderUserName = loanRequest.getLoanProduct().getUserName();

            userServiceCommunication
                    .sendMail(
                            MailRequestDto
                                    .builder()
                                    .subject("Offer rejection")
                                    .userId(loanRequest.getLoanProduct().getUserId())
                                    .message(
                                            "<p style=\"line-height: 1.5;\">Your offer is rejected to the loan application <strong>" +
                                                    loanApplication.getLoanApplicationName() +
                                                    "</strong> for the loan product <strong>" +
                                                    loanRequest.getLoanProduct().getLoanProductName() + "</strong>.</p>")
                                    .build()
                    );

            return loanRequestWithLoanProductMapper.toLoanRequestResponseWithLoanProductDto(
                    loanRequestMapper.toDto(loanRequest),
                    lenderUserName,
                    loanRequest.getLoanProduct().getLoanProductName());
        }
    }

    @Transactional
    @Override
    public LoanRequestResponseDto changeLoanRequestStatusToAccept(
            LoanRequestAcceptRequestDto loanRequestAcceptRequestDto, String role, UUID userId) {

        LoanRequest loanRequest = getLoanRequestById(loanRequestAcceptRequestDto.getLoanRequestId());

        userServiceCommunication.isBankAccountLinkWithUserId(userId, loanRequestAcceptRequestDto.getReceiverAccountId());

        LoanApplication loanApplication = loanRequest.getLoanApplication();

        if (loanRequest.getStatus() != LoanRequestStatus.OFFERED) {
            throw new BadRequestException("Loan request should be in offered state.");
        }

        if (!loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("Loan application with id :" + loanApplication.getLoanApplicationId() + " is not owned by user :" + userId);
        }

        loanRequest.setStatus(LoanRequestStatus.ACCEPTED);
        loanRequestRepository.save(loanRequest);

        List<LoanRequest> loanRequestList = loanRequestRepository
                .findByLoanApplicationLoanApplicationId(
                        loanApplication.getLoanApplicationId());

        historyLoanRequestService.saveLoanAllRequestForHistory(loanRequestList);

        HistoryLoanRequest historyLoanRequest = historyLoanRequestService.getHistoryLoanRequestById(
                loanRequestAcceptRequestDto.getLoanRequestId());
        historyLoanRequest.setReceiverAccountId(loanRequestAcceptRequestDto.getReceiverAccountId());
        historyLoanRequest = historyLoanRequestService.save(historyLoanRequest);

        loanRequestRepository.deleteAll(loanRequestList);
        SignedDocument signedDocument = signedDocumentService.generateSignedDocument(loanRequest, loanApplication, loanRequest.getLoanProduct());
        loanApplicationService.saveSignedDocument(loanApplication, signedDocument);

        String lenderUserName = loanRequest.getLoanProduct().getUserName();

        userServiceCommunication
                .sendMail(
                        MailRequestDto
                                .builder()
                                .subject("Offer accepted")
                                .userId(historyLoanRequest.getLoanProduct().getUserId())
                                .message(
                                        "<p style=\"line-height: 1.5;\">Your offer is accepted to the loan application <strong>" +
                                                loanApplication.getLoanApplicationName() +
                                                "</strong> for the loan product <strong>" +
                                                historyLoanRequest.getLoanProduct().getLoanProductName() + "</strong>.</p>")
                                .build()
                );

        userServiceCommunication
                .sendMail(
                        MailRequestDto
                                .builder()
                                .subject("Sign loan agreement")
                                .userId(historyLoanRequest.getLoanApplication().getUserId())
                                .message("<p style=\"line-height: 1.5;\">Please sign the Loan agreement to the loan product <strong>" +
                                        historyLoanRequest.getLoanProduct().getLoanProductName() +
                                        "</strong> for the loan application <strong>" +
                                        loanApplication.getLoanApplicationName() + "</strong>.</p>")

                                .build()
                );

        return loanRequestWithLoanProductMapper.toLoanRequestResponseWithLoanProductDto(
                historyLoanRequestMapper.toDto(historyLoanRequest),
                lenderUserName,
                loanRequest.getLoanProduct().getLoanProductName());
    }

    @Override
    public Long getLoanProductCountByUserIdAndLoanRequestStatus(UUID userId, LoanRequestStatus loanRequestStatus) {
        return loanRequestRepository
                .countByLoanProductUserIdAndStatus(userId, loanRequestStatus);
    }

    @Override
    public Boolean isNewRequestAllowedForLoanApplicationId(UUID loanApplicationId, UUID userId) {

        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);

        if (!loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("Loan application with id :" + loanApplication.getLoanApplicationId() + " is not owned by user :" + userId);
        }
        return !loanRequestRepository.existsByLoanApplicationLoanApplicationIdAndStatusIn(
                loanApplicationId,
                Arrays.asList(LoanRequestStatus.REQUESTED, LoanRequestStatus.OFFERED));
    }

}
