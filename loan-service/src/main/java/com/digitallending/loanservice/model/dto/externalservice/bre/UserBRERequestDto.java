package com.digitallending.loanservice.model.dto.externalservice.bre;

import lombok.Data;

@Data
public class UserBRERequestDto {
    private Integer age;
    private String gender;
    private String educationalQualification;
    private String maritalStatus;
    private String category;
    private Integer businessExperience;
    private Integer businessVintage;
}
