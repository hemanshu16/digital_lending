package com.digitallending.loanservice.model.dto;

import com.digitallending.loanservice.model.enums.LoanTypeName;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoanTypeResponseDto {
    private UUID loanTypeId;
    private LoanTypeName loanTypeName;
}
