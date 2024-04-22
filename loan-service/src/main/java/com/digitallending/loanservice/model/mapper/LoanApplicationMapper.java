package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.externalservice.bre.LoanApplicationBRERequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.LoanApplicationResponseDto;
import com.digitallending.loanservice.model.entity.LoanApplication;
import com.digitallending.loanservice.util.DateUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Date;


@Mapper(componentModel = "spring", uses = {
        PropertyDetailsMapper.class,
        SignedDocumentMapper.class})
public interface LoanApplicationMapper {
    @Mapping(source = "loanType.loanTypeName",target = "loanTypeName")
    @Mapping(source = "propertyDetails", target = "propertyDetailsResponseDto")
    @Mapping(source = "signedDocument", target = "signedDocumentResponseDto")
    LoanApplicationResponseDto toResponseDto(LoanApplication loanApplication);
    LoanApplication requestDtoTo(LoanApplicationRequestDto loanApplicationRequestDto);

    @Mapping(source = "propertyDetails.propertyVintage", target = "ownershipVintage", qualifiedByName = "mapPropertyVintage")
    @Mapping(source = "propertyDetails.propertyValuation",target = "propertyValuation")
    @Mapping(source = "propertyDetails.propertyType.propertyTypeName",target = "propertyType")
    LoanApplicationBRERequestDto loanApplicationToLoanApplicationBRERequestDto(LoanApplication loanApplication);

    @Named(value = "mapPropertyVintage")
    default Integer mapPropertyVintage(Date propertyVintage){
        return DateUtil.getMonthDifference(new Date(),propertyVintage);
    }
}