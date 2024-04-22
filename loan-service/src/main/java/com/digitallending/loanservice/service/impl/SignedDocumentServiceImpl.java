package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.InternalServerError;
import com.digitallending.loanservice.exception.SignedDocumentNotFoundException;
import com.digitallending.loanservice.model.dto.MailRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationStatusRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentPaginationResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentResponseForAdmin;
import com.digitallending.loanservice.model.dto.loanapplication.signeddocument.SignedDocumentStatusRequestDto;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanProduct;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.entity.SignedDocument;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import com.digitallending.loanservice.model.mapper.SignedDocumentForAdminMapper;
import com.digitallending.loanservice.model.mapper.SignedDocumentMapper;
import com.digitallending.loanservice.repository.SignedDocumentRepository;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.SignedDocumentService;
import com.digitallending.loanservice.service.externalservicecommunication.UserServiceCommunication;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SignedDocumentServiceImpl implements SignedDocumentService {

    @Autowired
    private SignedDocumentRepository signedDocumentRepository;

    @Autowired
    private SignedDocumentMapper signedDocumentMapper;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    @Lazy
    private HistoryLoanRequestService historyLoanRequestService;

    @Autowired
    private UserServiceCommunication userServiceCommunication;

    @Autowired
    private SignedDocumentForAdminMapper signedDocumentForAdminMapper;


    @Override
    public SignedDocument generateSignedDocument(LoanRequest loanRequest, LoanApplication loanApplication, LoanProduct loanProduct) {

        String lenderName = loanProduct.getUserName();
        String MSMEName = loanApplication.getUserName();

        double monthlyInterestRate = (loanRequest.getInterestRate() / 12) / 100;
        Long numberOfPayments = loanApplication.getTenure();

        double emi = (loanApplication.getAmount() * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
                / (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
        String formattedEMI = String.format("%.2f", emi);

        String documentContent = "<center><h2>Loan Agreement</h2></center>" +
                "    <p style=\"text-indent: 50px;\">This Loan Agreement is entered into " + LocalDate.now() + " , by and between " + lenderName + "  and " + MSMEName + ", individually referred to as \"Party\", and collectively \"the Parties\".</p>" +
                "&nbsp;" +
                "<p>WHEREAS, the Borrower desires to borrow a fixed amount of money and</p>" +
                "<p>WHEREAS, The Lender agrees to lend a fixed amount of money</p>" +
                "<p>IN CONSIDERATION of, the mutual promises, covenants, and conditions contained herein, the Parties agree as follows:</p>" +
                "<p><strong>&nbsp;</strong></p>" +
                "<ol>" +
                "    <li>" +
                "        <p>Loan Amount. The Parties agree the Lender will loan the Borrower ₹" + loanApplication.getAmount() + ".</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Interest Rate. The Parties agree the Interest Rate for this loan shall be " + loanRequest.getInterestRate() + "% to be accrued monthly.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Loan Term. This Loan shall be for a period of " + loanApplication.getTenure() + " months.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Repayment. The Parties agree the Borrower shall pay the Lender ₹" + formattedEMI + " per month. </p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Prepayment. The Borrower will not be penalized for early payment.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Representations and Warranties. Both Parties represent that they are fully authorized to enter into this Agreement. The performance and obligations of either Party will not violate or infringe upon the rights of any third party or violate any other agreement between the Parties, individually, and any other person, organization, or business or any law or governmental regulation.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Severability. In the event any provision of this Agreement is deemed invalid or unenforceable, in whole or in part, that part shall be severed from the remainder of the Agreement and all other provisions shall continue in full force and effect as valid and enforceable.&nbsp;</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Waiver. The failure by either party to exercise any right, power, or privilege under the terms of this Agreement will not be construed as a waiver of any subsequent or future exercise of that right, power, or privilege or the exercise of any other right, power, or privilege.&nbsp;</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Legal Fees. In the event of a dispute resulting in legal action, the successful Party will be entitled to its legal fees, including, but not limited to its attorneys’ fees.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Legal and Binding Agreement. This Agreement is legal and binding between the Parties as stated above. This Agreement may be entered into and is legal and binding both in the United States and throughout Europe. The Parties each represent that they have the authority to enter into this Agreement.</p>" +
                "    </li>" +
                "    <li>" +
                "        <p>Entire Agreement. The Parties acknowledge and agree that this Agreement represents the entire agreement between the Parties. In the event that the Parties desire to change, add, or otherwise modify any terms, they shall do so in writing to be signed by both parties.</p>" +
                "    </li>" +
                "</ol>" +
                "<p><strong>&nbsp;</strong></p>" +
                "&nbsp;" +
                "<p>The Parties agree to the terms and conditions set forth above as demonstrated by their signatures as follows:</p>" +
                "<p><strong><br><br></strong></p>" +
                "<p>Lender</p>" +
                "<p>Name: " + lenderName + "</p>" +
                "<p>Signed: _____________________________________</p>" +
                "<p>Date: _____________________________________</p>" +
                "<p><strong><br><br></strong></p>" +
                "<p>Borrower</p>" +
                "<p>Name: " + MSMEName + "</p>" +
                "<p>Signed: _____________________________________</p>" +
                "<p>Date: _____________________________________</p>" +
                "&nbsp;" +
                "</div>";

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        HtmlConverter.convertToPdf(documentContent, target);
        byte[] loanAgreement = target.toByteArray();
        return SignedDocument
                .builder()
                .signedDocumentId(loanApplication.getLoanApplicationId())
                .status(SignedDocumentStatus.UNSIGNED)
                .documentContent(loanAgreement)
                .build();
    }

    @Transactional
    @Override
    public String uploadSignedDocument(
            SignedDocumentRequestDto signedDocumentRequestDto,
            MultipartFile file,
            String role,
            UUID userId) {

        HistoryLoanRequest historyLoanRequest = historyLoanRequestService
                .getHistoryLoanRequestById(signedDocumentRequestDto.getLoanRequestId());

        SignedDocument signedDocument = historyLoanRequest.getLoanApplication().getSignedDocument();
        if (signedDocument == null) {
            throw new SignedDocumentNotFoundException("Document not found.");
        }


        if (file.isEmpty()) {
            throw new BadRequestException("File can not be empty");
        }
        if (Role.valueOf(role).equals(Role.MSME)) {
            if (signedDocument.getStatus() != SignedDocumentStatus.UNSIGNED) {
                throw new BadRequestException("Document is already signed by MSME");
            }

            if (!historyLoanRequest.getLoanApplication().getUserId().equals(userId)) {
                throw new BadRequestException("Loan application is not owned by user :" + userId);
            }

            signedDocument.setStatus(SignedDocumentStatus.SIGNED_BY_MSME);
            try {
                signedDocument.setDocumentContent(file.getBytes());
            } catch (IOException e) {
                throw new InternalServerError("Unable to read file");
            }
            signedDocument = signedDocumentRepository.save(signedDocument);
            userServiceCommunication
                    .sendMail(
                            MailRequestDto
                                    .builder()
                                    .subject("Sign loan agreement")
                                    .userId(historyLoanRequest.getLoanProduct().getUserId())
                                    .message("<p style=\"line-height: 1.5;\">Please sign the Loan agreement to the loan application <strong>" +
                                            historyLoanRequest.getLoanApplication().getLoanApplicationName() +
                                            "</strong> for the loan product <strong>" +
                                            historyLoanRequest.getLoanProduct().getLoanProductName() + "</strong>.</p>")
                                    .build());
        } else {
//          Role: Lender

            if (signedDocument.getStatus() != SignedDocumentStatus.SIGNED_BY_MSME) {
                throw new BadRequestException("Document should be signed by MSME");
            }

            if (!historyLoanRequest.getLoanProduct().getUserId().equals(userId)) {
                throw new BadRequestException("Loan product is not owned by user :" + userId);
            }
            signedDocument.setStatus(SignedDocumentStatus.SIGNED_BY_MSME_AND_LENDER);
            try {
                signedDocument.setDocumentContent(file.getBytes());
            } catch (IOException e) {
                throw new InternalServerError("Unable to read file");
            }
            signedDocument = signedDocumentRepository.save(signedDocument);
        }
        return "sign document is uploaded sucessfully";
    }

    @Transactional
    @Override
    public SignedDocumentPaginationResponseDto getAllUnapprovedSignDocument(int pageSize, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SignedDocument> pageResponse = signedDocumentRepository
                .findAllByStatus(SignedDocumentStatus.SIGNED_BY_MSME_AND_LENDER, pageable);


        List<SignedDocumentResponseForAdmin> signedDocumentResponseForAdminList =
                pageResponse
                        .getContent()
                        .stream()
                        .map(signedDocument -> {
                            LoanApplication loanApplication = signedDocument.getLoanApplication();

                            HistoryLoanRequest historyLoanRequest = historyLoanRequestService
                                    .findByLoanApplicationLoanApplicationIdAndStatus(
                                            loanApplication.getLoanApplicationId(),
                                            LoanRequestStatus.ACCEPTED).get(0);
                            return signedDocumentForAdminMapper.toSignedDocumentResponseForAdmin(
                                    signedDocumentMapper.toSignedDocumentResponseDto(signedDocument),
                                    historyLoanRequest.getLoanProduct().getUserName(),
                                    loanApplication.getUserName(),
                                    historyLoanRequest.getInterestRate(),
                                    loanApplication.getAmount(),
                                    loanApplication.getTenure());

                        })
                        .toList();
        return SignedDocumentPaginationResponseDto
                .builder()
                .signedDocumentResponseForAdminList(signedDocumentResponseForAdminList)
                .pageNo(pageResponse.getNumber())
                .pageSize(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .isLast(pageResponse.isLast())
                .build();
    }

    @Override
    @Transactional
    public String changeSignedDocumentStatus(SignedDocumentStatusRequestDto signedDocumentStatusRequestDto) {

        SignedDocument signedDocument = signedDocumentRepository.findById(signedDocumentStatusRequestDto.getSignedDocumentId())
                .orElseThrow(() -> new SignedDocumentNotFoundException("Signed document not found."));

        if (signedDocument.getStatus() != SignedDocumentStatus.SIGNED_BY_MSME_AND_LENDER) {
            throw new BadRequestException("Signed document should be signed by MSME and LENDER");
        }

        HistoryLoanRequest historyLoanRequest = historyLoanRequestService
                .findByLoanApplicationLoanApplicationIdAndStatus(
                        signedDocument.getLoanApplication().getLoanApplicationId(),
                        LoanRequestStatus.ACCEPTED).get(0);

        if (signedDocumentStatusRequestDto.getSignedDocumentStatus() == SignedDocumentStatus.RESIGNED) {
            signedDocumentRepository.changeSignedDocumentStatusById(
                    signedDocumentStatusRequestDto.getSignedDocumentId(),
                    SignedDocumentStatus.UNSIGNED);

            MailRequestDto mailRequestDto = new MailRequestDto();
            mailRequestDto.setUserId(historyLoanRequest.getLoanProduct().getUserId());
            mailRequestDto.setSubject("Resign the loan agreement");
            mailRequestDto.setMessage("<p style=\"line-height: 1.5;\"> It seems that the admin has rejected the loan agreement you signed for the loan application " +
                    historyLoanRequest.getLoanApplication().getLoanApplicationName() + ", so you need to restart signing process again.\n<strong>" +
                    "</strong> for the loan product <strong>" +
                    historyLoanRequest.getLoanProduct().getLoanProductName() + "</strong>.</p>");

            userServiceCommunication.sendMail(mailRequestDto);

            mailRequestDto.setUserId(historyLoanRequest.getLoanApplication().getUserId());

            userServiceCommunication.sendMail(mailRequestDto);

        } else {
            signedDocumentRepository.changeSignedDocumentStatusById(
                    signedDocumentStatusRequestDto.getSignedDocumentId(),
                    SignedDocumentStatus.APPROVED);

            loanApplicationService.updateLoanApplicationStatus(
                    LoanApplicationStatusRequestDto.builder()
                            .loanApplicationId(signedDocument.getLoanApplication().getLoanApplicationId())
                            .loanApplicationStatus(LoanApplicationStatus.APPROVED)
                            .message("your loan agreement is approved by admin")
                            .build()
            );

            userServiceCommunication.sendMail(
                    MailRequestDto
                            .builder()
                            .subject("Disburse the loan amount")
                            .userId(historyLoanRequest.getLoanProduct().getUserId())
                            .message("<p style=\"line-height: 1.5;\">Please disburse the loan amount to the loan application <strong>" +
                                    signedDocument.getLoanApplication().getLoanApplicationName() +
                                    "</strong> for the loan product <strong>" +
                                    historyLoanRequest.getLoanProduct().getLoanProductName() + "</strong>.</p>")
                            .build()
            );
        }
        return "Sign document status changed successfully";
    }

}
