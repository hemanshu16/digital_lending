package com.digitallending.loanservice.model.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class LoanServiceError {
    private Map<String, String> error;
    private String uri;
}