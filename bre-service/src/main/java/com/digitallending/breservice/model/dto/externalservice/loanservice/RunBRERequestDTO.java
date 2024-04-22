package com.digitallending.breservice.model.dto.externalservice.loanservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Data
public class RunBRERequestDTO {

    @NotNull(message = "User request DTO is required")
    @Valid
    private UserRequestDTO userRequestDTO;

    @NotNull(message = "Loan application request DTO is required")
    @Valid
    private LoanApplicationRequestDTO loanApplicationRequestDTO;

    @NotEmpty(message = "Loan product ID list cannot be empty")
    private List<UUID> loanProductIdList;
}
