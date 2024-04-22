package com.digitallending.loanservice.model.dto.apiresponse;

import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@Data
public class ApiResponse<T> {
    private T payload;
    @Builder.Default
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
}
