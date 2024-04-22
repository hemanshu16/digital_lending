package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.exception.DocumentTypeNotFoundException;
import com.digitallending.loanservice.exception.InternalServerError;
import com.digitallending.loanservice.exception.InvalidInputException;
import com.digitallending.loanservice.exception.LoanRequestNotFoundException;
import com.digitallending.loanservice.model.dto.loanapplication.document.DocumentResponseDto;
import com.digitallending.loanservice.model.dto.loanapplication.document.ProvidedDocumentResponseDto;
import com.digitallending.loanservice.model.entity.Document;
import com.digitallending.loanservice.model.entity.DocumentType;
import com.digitallending.loanservice.model.entity.HistoryLoanRequest;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.LoanRequest;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.model.mapper.DocumentMapper;
import com.digitallending.loanservice.repository.DocumentRepository;
import com.digitallending.loanservice.repository.DocumentTypeRepository;
import com.digitallending.loanservice.service.def.DocumentService;
import com.digitallending.loanservice.service.def.HistoryLoanRequestService;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.LoanRequestService;
import com.digitallending.loanservice.service.def.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    @Lazy
    private LoanApplicationService loanApplicationService;
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private LoanRequestService loanRequestService;
    @Autowired
    private HistoryLoanRequestService historyLoanRequestService;

    @Override
    @Transactional
    public DocumentResponseDto getAllDocumentByLoanApplicationId(
            UUID loanApplicationId, UUID userId, Role role) {
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        if (role.equals(Role.MSME) && !loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("loan application is not owned by you");
        } else {
            List<ProvidedDocumentResponseDto> providedDocumentResponseDtoList = documentRepository
                    .findAllByLoanApplicationLoanApplicationId(loanApplicationId)
                    .stream()
                    .map(
                            document -> documentMapper.toResponseDto(document)
                    ).toList();

            List<DocumentType> requiredDocumentTypeList = new ArrayList<>();
            if (loanApplication.getLoanApplicationStage().equals(LoanApplicationStage.DOCUMENT_REMAINING)) {
                requiredDocumentTypeList = getRemainingDocumentTypeListByLoanApplicationId(loanApplicationId, userId, role);
            }

            return DocumentResponseDto
                    .builder()
                    .providedDocumentResponseDtoList(providedDocumentResponseDtoList)
                    .requiredDocumentTypeList(requiredDocumentTypeList)
                    .build();
        }
    }

    private List<DocumentType> getRemainingDocumentTypeListByLoanApplicationId(UUID loanApplicationId, UUID userId, Role role) {
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(loanApplicationId);
        if (role.equals(Role.MSME) && !loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("you are not owner of this loan application");
        }
        List<Document> providedDocumentList = documentRepository
                .findAllByLoanApplicationLoanApplicationId(loanApplicationId);
        List<DocumentType> requiredDocumentList = loanTypeService
                .getLoanTypeById(loanApplication.getLoanType().getLoanTypeId()).getDocumentTypeList();

        Map<UUID, DocumentType> remainingDocumentTypeMap = new HashMap<>();

        requiredDocumentList
                .forEach(
                        documentType ->
                                remainingDocumentTypeMap.put(documentType.getDocumentTypeId(), documentType)
                );

        providedDocumentList
                .forEach(
                        document ->
                                remainingDocumentTypeMap.remove(document.getDocumentType().getDocumentTypeId())
                );
        return new ArrayList<>(remainingDocumentTypeMap.values());
    }

    @Override
    @Transactional
    public List<ProvidedDocumentResponseDto> getAllDocumentByLoanRequestId(UUID loanRequestId, UUID userId, Role role) {
        UUID loanApplicationId = null;
        try {
            LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);
            if (role.equals(Role.LENDER) && !loanRequest.getLoanProduct().getUserId().equals(userId)) {
                throw new BadRequestException("you are not owner of the loan product");
            }
            loanApplicationId = loanRequest.getLoanApplication().getLoanApplicationId();
        }catch(LoanRequestNotFoundException loanRequestNotFoundException){
            HistoryLoanRequest historyLoanRequest = historyLoanRequestService.getHistoryLoanRequestById(loanRequestId);
            if (role.equals(Role.LENDER) && !historyLoanRequest.getLoanProduct().getUserId().equals(userId)) {
                throw new BadRequestException("you are not owner of the loan product");
            }
            loanApplicationId = historyLoanRequest.getLoanApplication().getLoanApplicationId();
        }
        return documentRepository
                .findAllByLoanApplicationLoanApplicationId(loanApplicationId)
                .stream()
                .map(
                        document -> documentMapper.toResponseDto(document)
                ).toList();

    }

    @Override
    public void deleteExtraDocumentBasedOnUpdatedLoanType(UUID loanApplicationId, LoanType loanType, LoanType updatedLoanType) {
        if (!loanType.equals(updatedLoanType)) {
            List<Document> providedDocumentList = documentRepository.findAllByLoanApplicationLoanApplicationId(loanApplicationId);
            List<DocumentType> requiredDocumentList = loanTypeService.getAllRequiredDocumentByLoanTypeId(updatedLoanType.getLoanTypeId());
            Map<UUID, Document> providedDocumentMap = new HashMap<>();

            providedDocumentList
                    .forEach(
                            document -> providedDocumentMap.put(document.getDocumentType().getDocumentTypeId(), document)
                    );

            requiredDocumentList
                    .forEach(
                            requiredDocument -> providedDocumentMap.remove(requiredDocument.getDocumentTypeId())
                    );

            providedDocumentMap
                    .forEach((documentTypeId, document) -> documentRepository.deleteById(document.getDocumentId()));
        }
    }

    @Override
    public Long getCountByLoanApplicationId(UUID loanApplicationId) {
        return documentRepository
                .countByLoanApplicationLoanApplicationId(loanApplicationId);
    }

    @Override
    @Transactional
    public String saveAndUpdateDocument(MultipartFile file, UUID documentTypeId, UUID loanApplicationId, UUID userId) {

        byte[] documentContent;
        try {
            if (file.isEmpty()) {
                throw new InvalidInputException("please provide file");
            }
            documentContent = file.getBytes();
        } catch (IOException ex) {
            throw new InternalServerError("facing issues while string a file to database");
        }

        LoanApplication loanApplication = loanApplicationService
                .getLoanApplicationById(loanApplicationId);

        if (!loanApplication.getUserId().equals(userId)) {
            throw new BadRequestException("loan application is not owned by you");
        } else if (loanApplication.getLoanApplicationStage().equals(LoanApplicationStage.DOCUMENT_REMAINING)) {

            DocumentType documentType = documentTypeRepository
                    .findById(documentTypeId)
                    .orElseThrow(() -> new DocumentTypeNotFoundException("document type is not found"));

//          check that given document is required for loan type
            if (!loanTypeService
                    .getAllRequiredDocumentByLoanTypeId(loanApplication.getLoanType().getLoanTypeId())
                    .contains(documentType)) {
                throw new BadRequestException("given document is not required");
            }

            Optional<Document> optionalDocument = documentRepository
                    .findByLoanApplicationLoanApplicationIdAndDocumentTypeDocumentTypeId(loanApplicationId, documentTypeId);

            Document document;
            if (optionalDocument.isPresent()) {
                document = optionalDocument.get();
                document.setDocumentContent(documentContent);
            } else {
                document = Document
                        .builder()
                        .documentContent(documentContent)
                        .documentType(documentType)
                        .loanApplication(loanApplication)
                        .build();
            }
            documentRepository.save(document);
        } else {
            throw new BadRequestException("you can not update loan application");
        }

        return "document is stored successfully";
    }


}
