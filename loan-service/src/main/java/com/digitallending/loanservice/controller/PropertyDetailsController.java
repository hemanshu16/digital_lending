package com.digitallending.loanservice.controller;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsRequestDto;
import com.digitallending.loanservice.model.dto.loanapplication.propertydetails.PropertyDetailsResponseDto;
import com.digitallending.loanservice.model.entity.PropertyType;
import com.digitallending.loanservice.repository.PropertyDetailsRepository;
import com.digitallending.loanservice.service.def.PropertyDetailsService;
import com.digitallending.loanservice.service.def.PropertyTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan/property-details")
public class PropertyDetailsController {
    @Autowired
    private PropertyDetailsService propertyDetailsService;
    @Autowired
    private PropertyTypeService propertyTypeService;
    @PostMapping
    public ResponseEntity<ApiResponse<PropertyDetailsResponseDto>> saveAndUpdatePropertyLoanApplication(
            @RequestBody @Valid PropertyDetailsRequestDto propertyDetailsRequestDto,
            @RequestHeader("UserId") UUID userId) {
        ApiResponse<PropertyDetailsResponseDto> apiResponse = ApiResponse
                .<PropertyDetailsResponseDto>builder()
                .payload(propertyDetailsService.saveAndUpdatePropertyLoanApplication(propertyDetailsRequestDto, userId))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/property-type")
    public ResponseEntity<ApiResponse<List<PropertyType>>> getAllPropertyType(){
        ApiResponse<List<PropertyType>> apiResponse = ApiResponse
                .<List<PropertyType>>builder()
                .payload(propertyTypeService.getAllPropertyType())
                .build();
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
