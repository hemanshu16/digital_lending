package com.digitallending.loanservice.model.dto.loanapplication.propertydetails;

import lombok.Data;

import java.util.Date;

@Data
public class PropertyDetailsResponseDto {
    private Date propertyVintage; // in months
    private Long propertyValuation; // in Rs.
    private String propertyTypeName;
}
