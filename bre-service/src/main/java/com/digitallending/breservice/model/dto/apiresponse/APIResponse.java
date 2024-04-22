package com.digitallending.breservice.model.dto.apiresponse;


import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@Data
public class APIResponse<T> {
    private T payload;
    @Builder.Default
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
}
