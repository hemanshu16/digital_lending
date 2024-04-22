package com.digitallending.loanservice.model.dto.loanstatistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanProductCount {
    private String loanProductName;
    private Long count;
}