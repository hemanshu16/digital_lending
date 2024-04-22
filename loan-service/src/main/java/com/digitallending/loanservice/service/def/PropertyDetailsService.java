package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsResponseDto;
import com.digitallending.loanservice.model.entity.PropertyType;

import java.util.List;
import java.util.UUID;

public interface PropertyDetailsService {
    PropertyDetailsResponseDto saveAndUpdatePropertyLoanApplication(PropertyDetailsRequestDto propertyDetailsRequestDto, UUID userId);
    void deletePropertyDetails(UUID propertyDetailsId);
}
