package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.BadRequestException;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsResponseDto;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.model.entity.PropertyDetails;
import com.digitallending.loanservice.model.entity.PropertyType;
import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import com.digitallending.loanservice.model.enums.LoanTypeName;
import com.digitallending.loanservice.model.mapper.PropertyDetailsMapper;
import com.digitallending.loanservice.repository.PropertyDetailsRepository;
import com.digitallending.loanservice.service.def.LoanApplicationService;
import com.digitallending.loanservice.service.def.PropertyDetailsService;
import com.digitallending.loanservice.service.def.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PropertyDetailsServiceImpl implements PropertyDetailsService {
    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;
    @Autowired
    private LoanApplicationService loanApplicationService;
    @Autowired
    private PropertyTypeService propertyTypeService;
    @Autowired
    private PropertyDetailsMapper propertyDetailsMapper;

    @Override
    public PropertyDetailsResponseDto saveAndUpdatePropertyLoanApplication(
            PropertyDetailsRequestDto propertyDetailsRequestDto,
            UUID userId) {
        LoanApplication loanApplication = loanApplicationService.getLoanApplicationById(propertyDetailsRequestDto.getLoanApplicationId());

        validateDetailsForSaveAndUpdate(loanApplication,userId);

        if( !loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.MODIFICATION_REQUIRED) &&
                !loanApplication.getLoanApplicationStatus().equals(LoanApplicationStatus.IN_PROCESS)){
            throw new BadRequestException("you can not modify the loan application");
        }

        //      there must not be any entry currently present inside PropertyLoanApplication DB with given loanApplicationId

        PropertyDetails propertyDetails = propertyDetailsRequestDtoToPropertyDetails(propertyDetailsRequestDto);

        loanApplicationService.updateLoanApplicationStage(
                loanApplication.getLoanApplicationId(),LoanApplicationStage.DOCUMENT_REMAINING);

        return propertyDetailsMapper.propertyDetailsToResponseDto(propertyDetailsRepository.save(propertyDetails));
    }

    @Override
    public void deletePropertyDetails(UUID propertyDetailsId) {
        propertyDetailsRepository.deleteById(propertyDetailsId);
    }

    private PropertyDetails propertyDetailsRequestDtoToPropertyDetails(
            PropertyDetailsRequestDto propertyDetailsRequestDto) {

        PropertyDetails propertyDetails = propertyDetailsMapper.propertyDetailsRequestDtoToPropertyDetails(propertyDetailsRequestDto);


//        finding property type based on the given property type id
        PropertyType propertyType = propertyTypeService.getPropertyTypeById(propertyDetailsRequestDto.getPropertyTypeId());

//        set property type object in to the propertyLoanApplication
        propertyDetails.setPropertyType(propertyType);

        return propertyDetails;
    }

    private void validateDetailsForSaveAndUpdate(LoanApplication loanApplication,UUID userId){

//      SECURITY CHECK :- THIS LOAN APPLICATION MUST BELONG TO CURRENT LOG IN USER

        if (!loanApplication.getUserId().equals(userId)) {
//            rather than telling user that this loan application not belongs to you,
//              we will say that this loan application is not exists at all ( reason :- security )
            throw new BadRequestException("you are not owner of this loan application");
        }

//      we are checking type of loan
        if (!loanApplication.getLoanType().getLoanTypeName().equals(LoanTypeName.PROPERTY_LOAN)) {
            throw new BadRequestException("type of loan application is not PROPERTY_LOAN");
        }
    }

}
