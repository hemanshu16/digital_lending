package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsResponseDto;
import com.digitallending.loanservice.model.entity.PropertyDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyDetailsMapper {
    @Mapping(source = "propertyType.propertyTypeName", target = "propertyTypeName")
    PropertyDetailsResponseDto propertyDetailsToResponseDto(PropertyDetails propertyDetails);

    @Mapping(source = "loanApplicationId", target = "propertyDetailsId")
    PropertyDetails propertyDetailsRequestDtoToPropertyDetails(PropertyDetailsRequestDto propertyDetailsRequestDto);
}
