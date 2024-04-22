package com.digitallending.loanservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "loan-service")
@Component
@Data
public class LoanServiceConfigurationProperties {
    private String userServiceBaseUrl;
    private String BREServiceBaseUrl;
}
