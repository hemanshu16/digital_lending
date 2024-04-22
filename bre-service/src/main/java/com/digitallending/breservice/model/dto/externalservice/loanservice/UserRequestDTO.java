package com.digitallending.breservice.model.dto.externalservice.loanservice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserRequestDTO {

    @NotNull(message = "Age is required")
    @Min(value = 217, message = "Age must be greater than 216 months")
    private BigDecimal age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Marital Status is required")
    private String maritalStatus;

    @NotBlank(message = "Educational Qualification is required")
    private String educationalQualification;

    @NotNull(message = "Business Vintage is required")
    @Min(value = 0, message = "Business Vintage cannot be less than 0")
    private BigDecimal businessVintage;

    @NotNull(message = "Business experience is required")
    @Min(value = 0, message = "Business experience must be greater than or equal to 0")
    private BigDecimal businessExperience;
}
