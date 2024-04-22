package com.digitallending.loanservice.service.externalservicecommunication;

import com.digitallending.loanservice.config.LoanServiceConfigurationProperties;
import com.digitallending.loanservice.exception.BankAccountNotFoundException;
import com.digitallending.loanservice.exception.ExternalServiceException;
import com.digitallending.loanservice.exception.InvalidOtpException;
import com.digitallending.loanservice.exception.UserNotFoundException;
import com.digitallending.loanservice.model.dto.MailRequestDto;
import com.digitallending.loanservice.model.dto.apiresponse.ApiResponse;
import com.digitallending.loanservice.model.dto.externalservice.BankAccountResponseDto;
import com.digitallending.loanservice.model.dto.externalservice.bre.UserBRERequestDto;
import com.digitallending.loanservice.model.dto.externalservice.transaction.SendTransactionRequestDto;
import com.digitallending.loanservice.model.dto.externalservice.transaction.VerifyTransactionRequestDto;
import com.digitallending.loanservice.model.enums.Role;
import com.digitallending.loanservice.retrofit.generator.ServiceGenerator;
import com.digitallending.loanservice.retrofit.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserServiceCommunication {
    @Autowired
    private LoanServiceConfigurationProperties loanServiceConfigurationProperties;
    private ServiceGenerator userServiceGenerator;

    @PostConstruct
    public void init() {
        userServiceGenerator =
                new ServiceGenerator(loanServiceConfigurationProperties.getUserServiceBaseUrl());
    }

    public UserBRERequestDto getBRERequestUserDto(UUID userId) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<UserBRERequestDto>> callSync = userService.getBRERequestUserDto(userId);
        try {
            Response<ApiResponse<UserBRERequestDto>> response = callSync.execute();

            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- " + userId + " is not found");
            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as excepted");
            } else {
                return response.body().getPayload();
            }
        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted");
        }
    }

    public String sendTransactionOtp(UUID userId) {
        UserService userService = userServiceGenerator.createService(UserService.class);

        Call<ApiResponse<String>> callSync = userService.sendTransactionOtp(new SendTransactionRequestDto(userId));
        try {
            Response<ApiResponse<String>> response = callSync.execute();

            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- "
                        + userId
                        + " is not exists");
            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            } else {
                return response.body().getPayload();
            }
        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted");
        }
    }

    public BankAccountResponseDto getBankAccountDetailsById(UUID bankAccountId) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<BankAccountResponseDto>> callSync = userService.getBankAccountDetails(bankAccountId);
        try {
            Response<ApiResponse<BankAccountResponseDto>> response = callSync.execute();

            if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            } else {
                return response.body().getPayload();
            }

        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted");
        }
    }

    public void isUserExists(UUID userId, Role role) {

        UserService userService = userServiceGenerator.createService(UserService.class);

        Call<ApiResponse<Boolean>> callSync = userService.isUserExists(userId, role);

        try {

            Response<ApiResponse<Boolean>> response = callSync.execute();

            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- "
                        + userId
                        + " is not exists");
            } else if (response.code() == 500) {
                throw new ExternalServiceException("user service is not working as excepted code is 500");
            }

        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted");
        }
    }

    public void verifyTransactionOtp(Integer otp, String txnId) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<Boolean>> callSync = userService.verifyTransactionOtp(new VerifyTransactionRequestDto(txnId, otp));

        try {
            Response<ApiResponse<Boolean>> response = callSync.execute();

            if (response.code() == 500) {
                throw new ExternalServiceException("user service is not working as ");
            } else if (response.code() != 200) {
                throw new InvalidOtpException("provided otp is not valid");
            }

        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted");
        }
    }


    public Role getUserRole(UUID userId) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<Role>> callSync = userService.getUserRole(userId);
        try {
            Response<ApiResponse<Role>> response = callSync.execute();

            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- "
                        + userId
                        + " is not exists");
            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            }
            return response.body().getPayload();
        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted while checking existence of user");
        }
    }

    public String getUserName(UUID userId) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<String>> callSync = userService.getUserName(userId);
        try {
            Response<ApiResponse<String>> response = callSync.execute();

            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- "
                        + userId
                        + " is not exists");
            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            }
            return response.body().getPayload();
        } catch (IOException e) {
            throw new ExternalServiceException("user service is not working as excepted while fetching username");
        }
    }

    public void isBankAccountLinkWithUserId(UUID userId, UUID bankAccountId) {

        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<Boolean>> callSync = userService.isBankAccountLinkWithUserId(userId, bankAccountId);
        try {
            Response<ApiResponse<Boolean>> response = callSync.execute();
            if (response.code() == 404) {
                throw new BankAccountNotFoundException("Given bank account id: "
                        + bankAccountId +
                        " is not linked with user id: " + userId);

            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            }

        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted while checking link of account number with user");
        }
    }

    public void sendMail(MailRequestDto mailRequestDTO) {
        UserService userService = userServiceGenerator.createService(UserService.class);
        Call<ApiResponse<Boolean>> callSync = userService.sendMail(mailRequestDTO);
        try {
            Response<ApiResponse<Boolean>> response = callSync.execute();
            if (response.code() == 404) {
                throw new UserNotFoundException("user with id :- "
                        + mailRequestDTO.getUserId()
                        + " is not exists");
            } else if (response.code() == 500 || response.body() == null) {
                throw new ExternalServiceException("user service is not working as expected");
            }
        } catch (IOException ex) {
            throw new ExternalServiceException("user service is not working as excepted while sending mail");
        }
    }
}
