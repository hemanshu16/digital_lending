package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ChangeUserStatusDTO {
    @NotBlank
    private UUID userId;
    @NotBlank
    private UserOnBoardingStatus onBoardingStatus;
    @NotBlank
    private String statement;
}
