package com.digitallending.breservice.model.dto.apiresponse;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Builder
@Data
public class BREError {
    private Map<String,String> error;
    private String uri;
}
