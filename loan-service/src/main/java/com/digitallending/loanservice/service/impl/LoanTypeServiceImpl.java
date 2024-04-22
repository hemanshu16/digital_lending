package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.LoanTypeNotFoundException;
import com.digitallending.loanservice.model.dto.LoanTypeResponseDto;
import com.digitallending.loanservice.model.entity.DocumentType;
import com.digitallending.loanservice.model.entity.LoanType;
import com.digitallending.loanservice.model.mapper.LoanTypeMapper;
import com.digitallending.loanservice.repository.LoanTypeRepository;
import com.digitallending.loanservice.service.def.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanTypeServiceImpl implements LoanTypeService {
    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private LoanTypeMapper loanTypeMapper;

    @Override
    public LoanType getLoanTypeById(UUID loanTypeId) {
        return loanTypeRepository
                .findById(loanTypeId)
                .orElseThrow(
                        () -> new LoanTypeNotFoundException("loan type with id :- " + loanTypeId + " is not found!!")
                );
    }

    @Override
    public List<LoanTypeResponseDto> getAllLoanType() {
        return loanTypeMapper.loanTypeListToloanTypeResponseDtoList(loanTypeRepository
                .findAll());
    }

    @Override
    public List<DocumentType> getAllRequiredDocumentByLoanTypeId(UUID loanTypeId) {
        Optional<LoanType> optionalLoanType = loanTypeRepository.findById(loanTypeId);
        if (optionalLoanType.isPresent()) {
            return optionalLoanType.get().getDocumentTypeList();
        } else {
            // Loan type not found, return an empty list
            return Collections.emptyList();
        }
    }

}
