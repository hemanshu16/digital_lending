package com.digitallending.loanservice.model.dto.loanapplication;

import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationStatusRequestDto {
    @NotNull(message = "please provide loan application id")
    private UUID loanApplicationId;
    private LoanApplicationStatus loanApplicationStatus;
    @NotBlank(message = "please provide message")
    private String message;
}
