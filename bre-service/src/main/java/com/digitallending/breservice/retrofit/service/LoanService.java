package com.digitallending.breservice.retrofit.service;

import com.digitallending.breservice.model.dto.apiresponse.APIResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.UUID;

public interface LoanService {
    @GET("/api/loan/loan-product/{loanProductId}/loan-type-id")
    Call<APIResponse<UUID>> getLoanTypeIdByLoanProductId(@Path("loanProductId") UUID loanProductId);
}
