package com.digitallending.userservice.service;

import com.digitallending.userservice.apidef.AadhaarOTP;
import com.digitallending.userservice.apidef.AadhaarOTPCredential;
import com.digitallending.userservice.apidef.AadhaarPANLink;
import com.digitallending.userservice.apidef.PANInformation;
import com.digitallending.userservice.config.UserServiceConfigurationProperties;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.AadhaarPANLinkException;
import com.digitallending.userservice.exception.ExternalServiceException;
import com.digitallending.userservice.exception.PANInformationVerifyException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.RegexMisMatchException;
import com.digitallending.userservice.exception.WrongAadhaarNoException;
import com.digitallending.userservice.exception.WrongOTPException;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANInfoDTO;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.AadhaarPANLinkResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadhaarpanlink.MessageDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AadhaarOTPCredentialDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.AccessTokenDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.TransactionIDResponseDTO;
import com.digitallending.userservice.model.dto.kyc.aadharotp.VerifyOTPReqeustDTO;
import com.digitallending.userservice.model.dto.kyc.mobileotp.VerifyMobileOTPDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationRequestDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.PANInformationResponseDTO;
import com.digitallending.userservice.model.dto.kyc.paninformation.Result;
import com.digitallending.userservice.model.dto.kyc.paninformation.SourceOutput;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.UserKYCDetails;
import com.digitallending.userservice.service.def.TwilioService;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.def.UserKYCDetailsService;
import com.digitallending.userservice.service.impl.KYCServiceImpl;
import com.digitallending.userservice.service.impl.RSAServiceImpl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class KYCServiceTest {

    private final UUID userId = UUID.randomUUID();
    private final String panNo = "ABCDE0000F";
    private final String aadhaarNo = "302700001111";
    private final String phoneNo = "+917878454545";
    private final UserDetails userDetails = UserDetails
            .builder()
            .userId(userId)
            .phoneNo(Long.valueOf(phoneNo))
            .build();
    @InjectMocks
    @Spy
    private KYCServiceImpl kycService;
    @Mock
    private Retrofit retrofitPanVerification;
    @Mock
    private Retrofit retrofitPanInformation;
    @Mock
    private Retrofit retrofitAadhaarOTPCredential;
    @Mock
    private Retrofit retrofitAadhaarOTP;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserKYCDetailsService userKYCDetailsService;
    @Mock
    private UserServiceConfigurationProperties userServiceConfigurationProperties;
    @Mock
    private TwilioService twilioService;
    @Mock
    private AadhaarOTPCredential aadhaarOTPCredential;
    @Mock
    private RSAServiceImpl rsaService;

    @BeforeEach
    public void init() {

        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    void sendMobileOTPTest() {
        when(userDetailsService.getUserStatus(any(UUID.class))).thenReturn(UserOnBoardingStatus.VERIFY_EMAIL);
        kycService.sendMobileOtp(phoneNo, UUID.randomUUID());
        verify(twilioService, times(1)).sendMobileOTP(phoneNo);
    }

    @Test
    void sendMobileOTPTestInValidUserStatus() {
        when(userDetailsService.getUserStatus(any(UUID.class))).thenReturn(UserOnBoardingStatus.SIGN_UP);
        try {
            kycService.sendMobileOtp(phoneNo, UUID.randomUUID());
            fail("For SignUp Onboarding Status Mobile No Send OTP allowed");
        } catch (PreviousStepsNotDoneException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Send Mobile OTP :- For Invalid User Status Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\n Message " + exception.getMessage());
        }

    }

    @Test
    void sendMobileOtpLoanDisbursalTest() {

        when(userDetailsService.getUserDetails(userId)).thenReturn(userDetails);
        kycService.sendMobileOtpLoanDisbursal(userId);
        verify(twilioService, times(1)).sendMobileOTP(phoneNo);
    }

    @Test
    void verifyMobileOtpTest() {

        VerifyMobileOTPDTO verifyMobileOTPDTO = VerifyMobileOTPDTO
                .builder()
                .mobileNumber(phoneNo)
                .otp("787878")
                .build();

        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_EMAIL);

        when(userDetailsService.getUserDetails(userId)).thenReturn(userDetails);

        ArgumentCaptor<UserDetails> argument = ArgumentCaptor.forClass(UserDetails.class);

        kycService.verifyMobileOtp(verifyMobileOTPDTO, userId);

        verify(userDetailsService, times(1)).updateUserDetails(argument.capture());

        assertEquals(UserOnBoardingStatus.VERIFY_PHONE, argument.getValue().getOnBoardingStatus());

        assertEquals(Long.valueOf(phoneNo), argument.getValue().getPhoneNo());
    }

    private void getAccessToken() {
        when(userServiceConfigurationProperties.getAadhaarClientId()).thenReturn("Aadhaar Client Id");

        when(userServiceConfigurationProperties.getAadhaarClientSecret()).thenReturn("Aadhaar Clinet Secret");

        when(retrofitAadhaarOTPCredential.create(AadhaarOTPCredential.class)).thenReturn(aadhaarOTPCredential);

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO("Access Token");

        Response<AccessTokenDTO> response = Response.success(accessTokenDTO);

        Call<AccessTokenDTO> accessTokenDTOCall = mock(Call.class);

        try {
            when(aadhaarOTPCredential.getaccessToken(any(AadhaarOTPCredentialDTO.class))).thenReturn(accessTokenDTOCall);
            when(accessTokenDTOCall.execute()).thenReturn(response);
        } catch (IOException e) {
            fail("While Getting AccessToken IOException Thrown");
        }
    }

    @Test
    void getAccessTokenTest()
    {

        AadhaarDTO aadhaarDTO = new AadhaarDTO("Encrypted Aaadhar No");

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo);

        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_PHONE);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PHONE);

        ArgumentCaptor<UserKYCDetails> argument = ArgumentCaptor.forClass(UserKYCDetails.class);

        when(userServiceConfigurationProperties.getAadhaarClientId()).thenReturn("Aadhaar Client Id");

        when(userServiceConfigurationProperties.getAadhaarClientSecret()).thenReturn("Aadhaar Clinet Secret");

        when(retrofitAadhaarOTPCredential.create(AadhaarOTPCredential.class)).thenReturn(aadhaarOTPCredential);

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO("Access Token");

        ResponseBody responseBody = ResponseBody.create("Provide Valid OTP", MediaType.parse("application/octet-stream"));

        Response<AccessTokenDTO> response = Response.error(500,responseBody);

        Call<AccessTokenDTO> accessTokenDTOCall = mock(Call.class);

        when(aadhaarOTPCredential.getaccessToken(any(AadhaarOTPCredentialDTO.class))).thenReturn(accessTokenDTOCall);


        // Test Case 1 :- Response Status is Not Expected

        try {
            when(accessTokenDTOCall.execute()).thenReturn(response);
        } catch (IOException e) {
            fail("IO Exception Getting Access Token for Aadhaar OTP");
        }

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("Get Access Token :- Response Status is not expected, Exception not thrown");
        } catch (ExternalServiceException e) {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Get Access Token :- Response Status is not expected, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case 2 :- IO Exception occurred During Retrofit Call

        try {
            when(accessTokenDTOCall.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            fail("IO Excetpion Getting Access Token for Aadhaar OTP");
        }

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("Get Access Token for Send Aadhaar OTP :- IO Exception Not Handled");
        } catch (ExternalServiceException e) {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Get Access Token :- IO Exception occurred During Retrofit Call, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }

    @Test
    void sendAadhaarOTPTest() {

        // Test Case 1 :- Success
        AadhaarDTO aadhaarDTO = new AadhaarDTO("Encrypted Aaadhar No");

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo);

        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_PHONE);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PHONE);

        ArgumentCaptor<UserKYCDetails> argument = ArgumentCaptor.forClass(UserKYCDetails.class);

        getAccessToken();

        AadhaarOTP aadhaarOTP = mock(AadhaarOTP.class);

        Call<TransactionIDResponseDTO> transactionIDResponseDTOCall = mock(Call.class);

        Response<TransactionIDResponseDTO> TransactionIdresponse = Response.success(new TransactionIDResponseDTO("Txn Id"));

        when(retrofitAadhaarOTP.create(AadhaarOTP.class)).thenReturn(aadhaarOTP);

        when(aadhaarOTP.generateOTP(any(AadhaarDTO.class), any(String.class))).thenReturn(transactionIDResponseDTOCall);

        try {
            when(transactionIDResponseDTOCall.execute()).thenReturn(TransactionIdresponse);
        } catch (IOException e) {
            fail("Sending Aadhaar OTP IOException Thrown");
        }

        TransactionIDResponseDTO transactionIDResponseDTO = kycService.sendAadhaarOTP(aadhaarDTO, userId);

        verify(userKYCDetailsService, times(1)).saveUserKYCDetailsByUserId(argument.capture());

        assertEquals(userId, argument.getValue().getUserId());

        assertEquals("Encrypted Aaadhar No", argument.getValue().getAadharNo());

        assertEquals("Txn Id", transactionIDResponseDTO.getTxnId());

        //Test Case :- 2 Wrong Aadhaar No

        when(rsaService.decodeMessage(any(String.class))).thenReturn("3027007778");

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("For Wrong Aadhaar No Exception Not Thrown");
        } catch (RegexMisMatchException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Send Aadhaar OTP :- For InValid Aadhaar No, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        //Test Case :- 3 Wrong UserOnBoarding Status

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.SIGN_UP);

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("Send Aadhar OTP :- For Wrong User Status Exception Not Thrown");
        } catch (PreviousStepsNotDoneException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Send Aadhaar OTP :- For Wrong User Status, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case :- 4 Response Status is Not 200

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PHONE);

        ResponseBody responseBody = ResponseBody.create("Internal Server Error", MediaType.parse("application/octet-stream"));

        Response<TransactionIDResponseDTO> errorResponse = Response.error(500, responseBody);

        try {
            when(transactionIDResponseDTOCall.execute()).thenReturn(errorResponse);
        } catch (IOException e) {
            fail("Sending Aadhaar OTP IOException Thrown");
        }

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("Send Aadhar OTP :- For Wrong Response Status Exception Not Thrown");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Send Aadhaar OTP :- For Wrong Response Status, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case :- 5 IO Exception occurred During Retrofit Call
        try {
            when(transactionIDResponseDTOCall.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            fail("Sending Aadhaar OTP IOException Thrown");
        }

        try {
            kycService.sendAadhaarOTP(aadhaarDTO, userId);
            fail("Send Aadhar OTP :- IO Exception Not Thrown");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Send Aadhaar OTP :- IO Exception occurred During Retrofit Call, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }


    @Test
    void verifyAadhaarOTP() {

        // Test Case 1 :- Success
        VerifyOTPReqeustDTO verifyOTPReqeustDTO = new VerifyOTPReqeustDTO("778877", "Txn Id", "Encrypted Aadhaar No");

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PHONE);

        UserKYCDetails userKYCDetails = UserKYCDetails.builder().userId(userId).aadharNo("Encrypted Aadhaar No").build();

        when(userKYCDetailsService.getUserKYCDetailsByUserId(userId)).thenReturn(userKYCDetails);

        getAccessToken();

        AadhaarOTP aadhaarOTP = mock(AadhaarOTP.class);

        Call<TransactionIDResponseDTO> transactionIDResponseDTOCall = mock(Call.class);

        Response<TransactionIDResponseDTO> TransactionIdresponse = Response.success(new TransactionIDResponseDTO("Txn Id"));

        when(retrofitAadhaarOTP.create(AadhaarOTP.class)).thenReturn(aadhaarOTP);

        when(aadhaarOTP.verifyOTP(any(VerifyOTPReqeustDTO.class), any(String.class))).thenReturn(transactionIDResponseDTOCall);

        try {
            when(transactionIDResponseDTOCall.execute()).thenReturn(TransactionIdresponse);
        } catch (IOException e) {
            fail("Sending Aadhaar OTP IOException Thrown");
        }

        kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);

        verify(userDetailsService, times(1)).updateUserStatus(userId, UserOnBoardingStatus.VERIFY_AADHAR);


        //Test Case :- 2 Wrong Aadhaar No
        when(rsaService.decodeMessage(any(String.class))).thenReturn("3027007778");

        try {
            kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
            fail("Verify Aadhaar OTP :- For Wrong Aadhaar No Exception Not Thrown");
        } catch (RegexMisMatchException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar OTP :- For InValid Aadhaar No, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }


        //Test Case :- 3 Wrong UserOnBoarding Status

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_EMAIL);

        try {
            kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
            fail("Verify Aadhar OTP :- For Wrong User Status Exception Not Thrown");
        } catch (PreviousStepsNotDoneException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar OTP :- For Wrong User Status, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }


        // Test Case :- 4 Send Aadhaar OTP and Verify Aadhaar OTP , Aadhaar No MisMatch

        when(rsaService.decodeMessage(any(String.class))).thenReturn(aadhaarNo).thenReturn("302700774444");

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PHONE);

        try {
            kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
            fail("Verify Aadhar OTP :- For Wrong Aadhaar Number Exception Not Thrown");
        } catch (WrongAadhaarNoException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar OTP :- For Wrong Aadhaar Number, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case 5 :- Invalid OTP Or Wrong Response Status

        verifyOTPReqeustDTO.setAadhaarNo(aadhaarNo);

        ResponseBody responseBody = ResponseBody.create("Provide Valid OTP", MediaType.parse("application/octet-stream"));

        Response<TransactionIDResponseDTO> errorResponse = Response.error(400, responseBody);

        try {
            when(transactionIDResponseDTOCall.execute()).thenReturn(errorResponse);
        } catch (IOException e) {
            fail("Verify Aadhaar OTP IOException Thrown");
        }

        try {
            kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
            fail("Verify Aadhar OTP :- For Wrong Aadhaar OTP Exception Not Thrown");
        } catch (WrongOTPException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar OTP :- For Wrong Aadhaar OTP, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case 6 :-  IO Exception occurred During Retrofit Call

        try {
            when(transactionIDResponseDTOCall.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            fail("Verify Aadhaar OTP IOException Thrown");
        }

        try {
            kycService.verifyAadhaarOTP(verifyOTPReqeustDTO, userId);
            fail("Verify Aadhar OTP :- IO Exception Not Thrown");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar OTP :- IO Exception occurred During Retrofit Call, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }

    @Test
    void verifyAadhaarPanLinkStatus() {

        // Test Case 1 :- Success
        AadhaarPANLink aadhaarPANLink = mock(AadhaarPANLink.class);
        when(retrofitPanVerification.create(AadhaarPANLink.class)).thenReturn(aadhaarPANLink);
        when(userServiceConfigurationProperties.getRapidApiKey()).thenReturn("Rapid API Key");
        Call<AadhaarPANLinkResponseDTO> aadhaarPanLinkStatus = mock(Call.class);
        when(aadhaarPANLink.getAadharPANLinkStatus(any(AadhaarPANInfoDTO.class), anyString())).thenReturn(aadhaarPanLinkStatus);

        MessageDTO messageDTO = new MessageDTO("EF40124", "Type", "DESC");
        AadhaarPANLinkResponseDTO aadhaarPANLinkResponseDTO = new AadhaarPANLinkResponseDTO();
        ArrayList<MessageDTO> messageDTOs = new ArrayList<>();
        messageDTOs.add(messageDTO);
        aadhaarPANLinkResponseDTO.setMessages(messageDTOs);

        Response<AadhaarPANLinkResponseDTO> response = Response.success(aadhaarPANLinkResponseDTO);

        try {
            when(aadhaarPanLinkStatus.execute()).thenReturn(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            kycService.verifyAadhaarPanLinkStatus(panNo, aadhaarNo);
            assertTrue(true);
        } catch (Exception exception) {
            fail("Aadhaar Pan Link Status Failed");
        }

        // Test Case :- 2 Pan Is Not Linked With Aadhaar
        messageDTO.setCode("EF40111");

        try {
            kycService.verifyAadhaarPanLinkStatus(panNo, aadhaarNo);
            assertTrue(true);
        } catch (AadhaarPANLinkException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Not Expected Status");
        }

        // Test Case :- 3 Wrong Response Status

        ResponseBody responseBody = ResponseBody.create("Internal Server Error", MediaType.parse("application/octet-stream"));
        response = Response.error(500, responseBody);
        try {
            when(aadhaarPanLinkStatus.execute()).thenReturn(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            kycService.verifyAadhaarPanLinkStatus(panNo, aadhaarNo);
            fail("Verify Aadhaar PAN Link Status :- Wrong Response Status Not Handled");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Not Expected Status");
        }

        // Test Case 4 :- IO Exception occurred During Retrofit Call
        try {
            when(aadhaarPanLinkStatus.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            kycService.verifyAadhaarPanLinkStatus(panNo, aadhaarNo);
            fail("Verify Aadhaar PAN Link Status :- IO Exception Not Handled");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify Aadhaar PAN Link Status :- IO Exception Not Handled, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }

    @Test
    void saveNameFromPan() {

        // Test Case 1 :- Success
        PANInformation panInformation = mock(PANInformation.class);
        when(retrofitPanInformation.create(PANInformation.class)).thenReturn(panInformation);
        when(userServiceConfigurationProperties.getRapidApiTaskId()).thenReturn("Rapid API Task Id");
        when(userServiceConfigurationProperties.getRapidApiGroupId()).thenReturn("Rapid API Group Id");
        when(userServiceConfigurationProperties.getRapidApiKey()).thenReturn("Rapid API Key");
        Call<PANInformationResponseDTO> panInformationResponse = mock(Call.class);
        when(panInformation.getAadharPanLinkStatus(any(PANInformationRequestDTO.class), anyString())).thenReturn(panInformationResponse);

        SourceOutput sourceOutput = new SourceOutput("Hemanshu", "Faldu", "Maheshbhai", "Hemanshu Maheshbhai Faldu");
        Result result = new Result(sourceOutput);
        PANInformationResponseDTO panInformationResponseDTO = new PANInformationResponseDTO("Status", result, "Message");
        Response<PANInformationResponseDTO> panInformationResponseDTOResponse = Response.success(panInformationResponseDTO);

        try {
            when(panInformationResponse.execute()).thenReturn(panInformationResponseDTOResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        userDetails.setFirstName("");
        userDetails.setLastName("");
        kycService.saveNameFromPan(panNo, userDetails);

        assertEquals("Hemanshu", userDetails.getFirstName());
        assertEquals("Faldu", userDetails.getLastName());

        // Test :- 2 Not Getting Pan Information
        panInformationResponseDTO.setStatus("failed");

        try {
            kycService.saveNameFromPan(panNo, userDetails);
            fail("Get Pan Information :- Fetch Pan Information failed but Exception Not Thrown");
        } catch (PANInformationVerifyException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Get Pan Information :- Fetch Pan Information failed, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test :- 3 Wrong Response Status Code

        panInformationResponseDTO.setStatus("success");
        ResponseBody responseBody = ResponseBody.create("Internal Server Error", MediaType.parse("application/octet-stream"));
        Response<PANInformationResponseDTO> errorResponse = Response.error(500, responseBody);

        try {
            when(panInformationResponse.execute()).thenReturn(errorResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            kycService.saveNameFromPan(panNo, userDetails);
            fail("Get Pan Information :- Wrong Response Status but Exception Not Thrown");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Get Pan Information :- Wrong Response Status, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test 4 :- IO Exception occurred During Retrofit Call
        try {
            when(panInformationResponse.execute()).thenThrow(new IOException());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            kycService.saveNameFromPan(panNo, userDetails);
            fail("Get Pan Information :- IO Exception Not Thrown");
        } catch (ExternalServiceException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Get Pan Information :- IO Exception occurred During Retrofit Call, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }

    @Test
    void verifyPan() {

        // Test Case 1 :- Success

        when(rsaService.decodeMessage(anyString())).thenReturn(panNo).thenReturn(aadhaarNo);

        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_AADHAR);

        when(userDetailsService.getUserDetails(userId)).thenReturn(userDetails);

        when(userKYCDetailsService.getUserKYCDetailsByUserId(userId)).thenReturn(UserKYCDetails.builder().aadharNo("Encrypted Aadhaar No").build());

        doNothing().when(kycService).verifyAadhaarPanLinkStatus(panNo, aadhaarNo);

        doNothing().when(kycService).saveNameFromPan(anyString(), any(UserDetails.class));

        when(userKYCDetailsService.saveUserKYCDetailsByUserId(any(UserKYCDetails.class)))
                .thenAnswer((Answer<UserKYCDetails>) invocation -> {
                    UserKYCDetails userKYCDetails = invocation.getArgument(0);
                    userKYCDetails.setUserId(userId);
                    assertEquals("Encrypted PAN No", userKYCDetails.getPanNo());
                    return userKYCDetails;
                });

        when(userDetailsService.updateUserDetails(ArgumentMatchers.any(UserDetails.class)))
                .thenAnswer((Answer<UserDetails>) invocation -> {
                    UserDetails userDetails = invocation.getArgument(0);
                    userDetails.setUserId(userId);
                    assertEquals(UserOnBoardingStatus.VERIFY_PAN, userDetails.getOnBoardingStatus());
                    return userDetails;
                });

        kycService.verifyPan("Encrypted PAN No", userId);


        // Test Case 2 :- Wrong Pan Number
        when(rsaService.decodeMessage(anyString())).thenReturn("HLPNN00000");
        userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_AADHAR);
        try {
            kycService.verifyPan("Encrypted PAN No", userId);
            fail("Verify PAN :- Wrong PAN Number But Exception Not Thrown");
        } catch (RegexMisMatchException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify PAN :- Wrong PAN Number, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

        // Test Case 3 :- Wrong User Onboarding Status
        userDetails.setOnBoardingStatus(UserOnBoardingStatus.BANK_DETAILS);

        when(rsaService.decodeMessage(anyString())).thenReturn(panNo).thenReturn(aadhaarNo);

        try {
            kycService.verifyPan("Encrypted PAN No", userId);
            fail("Verify PAN :- Wrong User OnBoarding Status But Exception Not Thrown");
        } catch (PreviousStepsNotDoneException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            fail("Verify PAN :- Wrong User OnBoarding Status, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    + "\nMessage " + exception.getMessage());
        }

    }

}
