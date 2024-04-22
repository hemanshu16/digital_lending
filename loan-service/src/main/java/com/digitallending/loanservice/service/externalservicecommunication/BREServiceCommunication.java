package com.digitallending.loanservice.service.externalservicecommunication;

import com.digitallending.loanservice.config.LoanServiceConfigurationProperties;
import com.digitallending.loanservice.exception.ExternalServiceException;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.externalservice.bre.BRERequestDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResult;
import com.digitallending.loanservice.retrofit.generator.ServiceGenerator;
import com.digitallending.loanservice.retrofit.service.BREService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Service
public class BREServiceCommunication {
    @Autowired
    private LoanServiceConfigurationProperties loanServiceConfigurationProperties;
    private ServiceGenerator BREServiceGenerator;

    @PostConstruct
    public void init() {
        BREServiceGenerator =
                new ServiceGenerator(loanServiceConfigurationProperties.getBREServiceBaseUrl());
    }

    public List<BREResult> getBREResult(BRERequestDto breRequestDto) {
        BREService breService = BREServiceGenerator.createService(BREService.class);

        Call<ApiResponse<List<BREResult>>> callSync = breService.getBREResultForAllProduct(breRequestDto);
        try {

            Response<ApiResponse<List<BREResult>>> response = callSync.execute();

            if (response.body() != null && response.code() == 200) {
                return response.body().getPayload();
            } else {
                throw new ExternalServiceException("bre service is not working as excepted");
            }
        } catch (IOException ex) {
            throw new ExternalServiceException("bre service is not working as excepted");
        }
    }
}
