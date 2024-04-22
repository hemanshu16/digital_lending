package com.digitallending.loanservice.model.dto.loanapplication.propertydetails;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data

public class PropertyDetailsRequestDto {

    @NotNull(message = "Loan application ID must not be blank")
    private UUID loanApplicationId;

    @NotNull(message = "Property vintage must not be null")
    @PastOrPresent(message = "please provide property date")
    private Date propertyVintage; // in month

    @NotNull(message = "Property valuation must not be null")
    @Min(value = 20_000,message = "property valuation can't be less than 20,000")
    private Long propertyValuation;

    @NotNull(message = "Property type ID must not be blank")
    private UUID propertyTypeId;
}
