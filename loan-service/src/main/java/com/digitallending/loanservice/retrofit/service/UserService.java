package com.digitallending.loanservice.retrofit.service;


import com.digitallending.loanservice.model.dto.MailRequestDto;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.externalservice.BankAccountResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.UserBRERequestDto;
import com.digitallending.loanservice.model.dto.externalservice.transaction.SendTransactionRequestDto;
import com.digitallending.loanservice.model.dto.externalservice.transaction.VerifyTransactionRequestDto;
import com.digitallending.loanservice.model.enums.Role;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.UUID;

public interface UserService {

    @GET("/api/user/check-user")
    Call<ApiResponse<Boolean>> isUserExists(@Query("userId") UUID userId,@Query("role") Role role);

    @GET("/api/user/{userId}/bre-details")
    Call<ApiResponse<UserBRERequestDto>> getBRERequestUserDto(@Path("userId") UUID userId);

    @POST("/api/user/transaction-otp")
    Call<ApiResponse<String>> sendTransactionOtp(@Body SendTransactionRequestDto sendTransactionRequestDto);

    @POST("/api/user/verify-transaction-otp")
    Call<ApiResponse<Boolean>> verifyTransactionOtp(@Body VerifyTransactionRequestDto verifyTransactionRequestDto);

    @GET("/api/user/bank-account/link-status")
    Call<ApiResponse<Boolean>> isBankAccountLinkWithUserId(
            @Query("userId") UUID userId,
            @Query("bankAccountId") UUID bankAccountId);

    @GET("/api/user/{userId}/role")
    Call<ApiResponse<Role>> getUserRole(@Path("userId") UUID userId);

    @GET("/api/user/{userId}/name")
    Call<ApiResponse<String>> getUserName(@Path("userId") UUID userId);

    @POST("/api/user/send-mail")
    Call<ApiResponse<Boolean>> sendMail(@Body MailRequestDto mailRequestDto);

    @GET("/api/user/bank-account/{bankAccountId}")
    Call<ApiResponse<BankAccountResponseDto>> getBankAccountDetails(@Path("bankAccountId") UUID bankAccountId);
}
