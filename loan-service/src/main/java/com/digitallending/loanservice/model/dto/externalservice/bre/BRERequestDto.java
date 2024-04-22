package com.digitallending.loanservice.model.dto.externalservice.bre;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
@Builder
public class BRERequestDto {
    private UserBRERequestDto userRequestDTO;
    private LoanApplicationBRERequestDto loanApplicationRequestDTO;
    private List<UUID> loanProductIdList;
}
