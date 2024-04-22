package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.InvalidInputException;
import com.digitallending.loanservice.exception.TransactionNotFoundException;
import com.digitallending.loanservice.model.dto.externalservice.BankAccountResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationStatusRequestDto;
import com.digitallending.loanservice.model.dto.transaction.InitiateTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.SubmitTransactionRequestDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionPaginationResponseDto;
import com.digitallending.loanservice.model.dto.transaction.TransactionResponseDto;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.entity.Transaction;
import com.digitallending.loanservice.model.entity.TransactionValidation;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.mapper.TransactionMapper;
import com.digitallending.loanservice.repository.TransactionRepository;
import com.digitallending.loanservice.repository.TransactionValidationRepository;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.TransactionService;
import com.digitallending.loanservice.service.externalservicecommunication.UserServiceCommunication;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service

public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;
    @Autowired
    private UserServiceCommunication userServiceCommunication;
    @Autowired
    private TransactionValidationRepository transactionValidationRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private LoanApplicationService loanApplicationService;

    @Override
    public TransactionPaginationResponseDto getAllTransaction(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> pageResponse = transactionRepository.findAll(pageable);

        List<TransactionResponseDto> transactionResponseDtoList = pageResponse
                .getContent()
                .stream()
                .map(transaction -> transactionMapper.transactionToTransactionResponseDto(transaction))
                .toList();

        return TransactionPaginationResponseDto
                .builder()
                .transactionResponseDtoList(transactionResponseDtoList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Override
    public TransactionPaginationResponseDto getAllTransactionByUserId(UUID userId, UUID headerUserId, Role userRole, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> pageResponse;
        Role role = userRole;
        UUID id = headerUserId;
        if (userRole.equals(Role.ADMIN)) {
            role = userServiceCommunication.getUserRole(userId);
            id = userId;
        }

        if (role.equals(Role.LENDER)) {
            pageResponse = transactionRepository
                    .findByLoanProduct_UserId(id, pageable);
        } else {
            pageResponse = transactionRepository
                    .findByLoanApplication_UserId(id, pageable);
        }

        List<TransactionResponseDto> transactionResponseDtoList = pageResponse
                .getContent()
                .stream()
                .map(transaction -> transactionMapper.transactionToTransactionResponseDto(transaction))
                .toList();

        return TransactionPaginationResponseDto
                .builder()
                .transactionResponseDtoList(transactionResponseDtoList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Override
    public TransactionResponseDto getTransactionByLoanApplicationId(UUID loanApplicationId) {
        Transaction transaction = transactionRepository
                .findByLoanApplication_LoanApplicationId(loanApplicationId)
                .orElseThrow(() -> new TransactionNotFoundException("there is not any transaction on given loan application id"));
        return transactionMapper.transactionToTransactionResponseDto(transaction);
    }

    @Override
    public TransactionPaginationResponseDto getTransactionByLoanProductId(UUID loanProductId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> pageResponse;
        pageResponse = transactionRepository
                .findByLoanProduct_LoanProductId(loanProductId, pageable);
        List<TransactionResponseDto> transactionResponseDtoList = pageResponse
                .getContent()
                .stream()
                .map(transaction -> transactionMapper.transactionToTransactionResponseDto(transaction))
                .toList();

        return TransactionPaginationResponseDto
                .builder()
                .transactionResponseDtoList(transactionResponseDtoList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Override
    @Transactional
    public String initiateTransaction(InitiateTransactionRequestDto initiateTransactionRequestDto, UUID userId) {

        HistoryLoanRequest historyLoanRequest = historyLoanRequestService
                .getHistoryLoanRequestById(initiateTransactionRequestDto.getHistoryLoanRequestId());

        if (!historyLoanRequest.getStatus().equals(LoanRequestStatus.ACCEPTED)) {
            throw new BadRequestException("you can not initiate txn for this loan");
        }

        LoanProduct loanProduct = historyLoanRequest.getLoanProduct();
        LoanApplication loanApplication = historyLoanRequest.getLoanApplication();

        if (loanProduct.getUserId().equals(userId)) {
            if (loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.APPROVED)) {

                userServiceCommunication.isBankAccountLinkWithUserId(userId, initiateTransactionRequestDto.getProviderAccountId());

                historyLoanRequestService.changeProviderAccountId(
                        initiateTransactionRequestDto.getHistoryLoanRequestId(),
                        initiateTransactionRequestDto.getProviderAccountId());

                String txnId = userServiceCommunication.sendTransactionOtp(userId);

                TransactionValidation transactionValidation = TransactionValidation
                        .builder()
                        .txnId(txnId)
                        .historyLoanRequestId(historyLoanRequest.getLoanRequestId())
                        .build();
                transactionValidationRepository.save(transactionValidation);

                return "transaction is initiated successfully please provide otp";
            } else {
                throw new BadRequestException("loan application is not approved by admin");
            }
        } else {
            throw new BadRequestException("you are not owner of the product which is present in history loan request");
        }
    }

    @Transactional
    @Override
    public String submitTransaction(UUID userId, SubmitTransactionRequestDto submitTransactionRequestDto) {

        TransactionValidation transactionValidation = transactionValidationRepository
                .findById(submitTransactionRequestDto.getHistoryLoanRequestId())
                .orElseThrow(() -> new InvalidInputException("transaction is not yet initiated"));

        userServiceCommunication.verifyTransactionOtp(submitTransactionRequestDto.getOtp(), transactionValidation.getTxnId());

        HistoryLoanRequest historyLoanRequest = historyLoanRequestService
                .getHistoryLoanRequestById(submitTransactionRequestDto.getHistoryLoanRequestId());

        LoanApplication loanApplication = historyLoanRequest.getLoanApplication();
        LoanProduct loanProduct = historyLoanRequest.getLoanProduct();

        if (!loanProduct.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan product");
        }

        BankAccountResponseDto receiverBankAccountDetails =
                userServiceCommunication
                        .getBankAccountDetailsById(historyLoanRequest.getReceiverAccountId());

        BankAccountResponseDto providerBankAccountDetails =
                userServiceCommunication
                        .getBankAccountDetailsById(historyLoanRequest.getProviderAccountId());

        Transaction transaction = Transaction
                .builder()
                .amount(loanApplication.getAmount())
                .loanApplication(loanApplication)
                .loanProduct(loanProduct)
                .providerAccountNo(providerBankAccountDetails.getAccountNumber())
                .providerAccountHolderName(providerBankAccountDetails.getAccountHolderName())
                .receiverAccountNo(receiverBankAccountDetails.getAccountNumber())
                .receiverAccountHolderName(receiverBankAccountDetails.getAccountHolderName())
                .build();
        transactionRepository.save(transaction);

        transactionValidationRepository.deleteById(transactionValidation.getHistoryLoanRequestId());

        loanApplicationService.updateLoanApplicationStatus(
                LoanApplicationStatusRequestDto
                        .builder()
                        .loanApplicationId(loanApplication.getLoanApplicationId())
                        .loanApplicationStatus(LoanApplicationStatus.DISBURSED)
                        .message("amount ₹" + loanApplication.getAmount() + " for loan application is disbursed from the loan product " + loanProduct.getLoanProductName())
                        .build()
        );

        return "amount is transferred successfully";
    }
}
