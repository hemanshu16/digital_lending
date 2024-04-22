package com.digitallending.loanservice.retrofit.service;

import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.externalservice.bre.BRERequestDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.BREResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface BREService {
    @POST("/api/bre/run")
    public Call<ApiResponse<List<BREResult>>> getBREResultForAllProduct(@Body BRERequestDto requestBody);
}
