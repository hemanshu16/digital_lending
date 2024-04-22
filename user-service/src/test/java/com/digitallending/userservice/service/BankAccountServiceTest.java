package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.BankAccountNotFoundException;
import com.digitallending.userservice.exception.InvalidUserException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.userdetails.BankAccountDTO;
import com.digitallending.userservice.model.entity.BankAccount;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.mapper.BankAccountMapper;
import com.digitallending.userservice.model.mapper.BankAccountMapperImpl;
import com.digitallending.userservice.providers.BankAccountProvider;
import com.digitallending.userservice.repository.BankAccountRepository;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountMapperImpl bankAccountMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void init() {

        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        ReflectionTestUtils.setField(bankAccountService, "bankAccountMapper", bankAccountMapper);
    }

    private UserDetails userDetails = UserDetails.builder()
            .userId(UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f25eb"))
            .onBoardingStatus(UserOnBoardingStatus.BUSINESS_DOC)
            .build();


    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void createBankAccountByUserIdTest(UUID userId, BankAccountDTO bankAccountDTO, Role role)
    {
        if(role == Role.LENDER)
        {
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.VERIFY_PAN);
        }
        else{
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.BUSINESS_DOC);
        }

        when(userDetailsService.getUserDetails(ArgumentMatchers.any(UUID.class))).thenReturn(userDetails);

        when(bankAccountRepository.save(ArgumentMatchers.any(BankAccount.class)))
                .thenAnswer(new Answer<BankAccount>() {
                    @Override
                    public BankAccount answer(InvocationOnMock invocation) throws Throwable {
                        BankAccount bankAccount = invocation.getArgument(0);
                        bankAccount.setBankAccountId(UUID.randomUUID());
                        assertEquals(bankAccount.getUser().getOnBoardingStatus(),UserOnBoardingStatus.BANK_DETAILS);
                        return bankAccount;
                    }
                });

        BankAccountDTO bankAccountResponseDTO = bankAccountService.createBankAccountByUserId(userId, bankAccountDTO, role);

        assertEquals(bankAccountResponseDTO.getAccountNumber(),bankAccountDTO.getAccountNumber());
        assertEquals(bankAccountResponseDTO.getAccountHolderName(),bankAccountDTO.getAccountHolderName());
        assertEquals(bankAccountResponseDTO.getIfscCode(),bankAccountDTO.getIfscCode());
        assertEquals(bankAccountResponseDTO.getBankName(),bankAccountDTO.getBankName());
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void createBankAccountByUserIdTestNotValidUserOnBoardingStatus(UUID userId, BankAccountDTO bankAccountDTO, Role role)
    {
        if(role.equals(Role.LENDER))
        {
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.USER_DOC);
        }
        else{
            userDetails.setOnBoardingStatus(UserOnBoardingStatus.USER_DOC);
        }

        when(userDetailsService.getUserDetails(ArgumentMatchers.any(UUID.class))).thenReturn(userDetails);

        try {
            BankAccountDTO bankAccountResponseDTO = bankAccountService.createBankAccountByUserId(userId, bankAccountDTO, role);
            fail("Wrong User Status But Update Allowed");
        }
        catch(InvalidUserException exception)
        {
            assertTrue(true);
        }
        catch (Exception exception)
        {
            fail("Create Bank Account :- For Invalid User Status Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }

    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void updateBankAccountByUserIdTest(UUID userId, BankAccountDTO bankAccountDTO, Role role)
    {
        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.BANK_DETAILS);

        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);

        when(bankAccountRepository.findByUserUserIdAndBankAccountId(userId,bankAccountDTO.getBankAccountId())).thenReturn(bankAccount);

        when(bankAccountRepository.save(ArgumentMatchers.any(BankAccount.class)))
                .thenAnswer(new Answer<BankAccount>() {
                    @Override
                    public BankAccount answer(InvocationOnMock invocation) throws Throwable {
                        BankAccount bankAccount = invocation.getArgument(0);
                        bankAccount.setBankAccountId(UUID.randomUUID());
                        return bankAccount;
                    }
                });

        BankAccountDTO bankAccountResponseDTO = bankAccountService.updateBankAccountByUserId(userId,bankAccountDTO.getBankAccountId(), bankAccountDTO);

        assertEquals(bankAccountResponseDTO.getAccountNumber(),bankAccountDTO.getAccountNumber());
        assertEquals(bankAccountResponseDTO.getAccountHolderName(),bankAccountDTO.getAccountHolderName());
        assertEquals(bankAccountResponseDTO.getIfscCode(),bankAccountDTO.getIfscCode());
        assertEquals(bankAccountResponseDTO.getBankName(),bankAccountDTO.getBankName());
    }


    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void updateBankAccountByUserIdTestNotValidUserStatus(UUID userId, BankAccountDTO bankAccountDTO, Role role)
    {
        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.USER_DETAILS);

        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);

        when(bankAccountRepository.findByUserUserIdAndBankAccountId(userId,bankAccountDTO.getBankAccountId())).thenReturn(bankAccount);

        try {
            BankAccountDTO bankAccountResponseDTO = bankAccountService.updateBankAccountByUserId(userId, bankAccountDTO.getBankAccountId(), bankAccountDTO);
            fail("For Invalid User Status Bank Account Update Allowed");
        }
        catch(UpdateException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Update Bank Account :- For Invalid User Status Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }

    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void updateBankAccountByUserIdTestBankAccountIdNotExist(UUID userId, BankAccountDTO bankAccountDTO, Role role)
    {
        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.BANK_DETAILS);
        when(bankAccountRepository.findByUserUserIdAndBankAccountId(userId,bankAccountDTO.getBankAccountId())).thenReturn(null);

        try {
            BankAccountDTO bankAccountResponseDTO = bankAccountService.updateBankAccountByUserId(userId, bankAccountDTO.getBankAccountId(), bankAccountDTO);
            fail("For InValid BankAccountId, Bank Account Update Allowed");
        }
        catch(BankAccountNotFoundException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Update Bank Account :- For InValid BankAccount Id, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }

    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void deleteBankAccountTest(UUID userId, BankAccountDTO bankAccountDTO)
    {
        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.BANK_DETAILS);
        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);
        when(bankAccountRepository.findByUserUserIdAndBankAccountId(userId,bankAccountDTO.getBankAccountId())).thenReturn(bankAccount);
        bankAccountService.deleteBankAccountByUserId(userId,bankAccountDTO.getBankAccountId());
        verify(bankAccountRepository,times(1)).delete(bankAccount);
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void deleteBankAccountTestNotValidBankAccountId(UUID userId, BankAccountDTO bankAccountDTO)
    {
        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.BANK_DETAILS);
        when(bankAccountRepository.findByUserUserIdAndBankAccountId(userId,bankAccountDTO.getBankAccountId())).thenReturn(null);
        try{
            bankAccountService.deleteBankAccountByUserId(userId,bankAccountDTO.getBankAccountId());
            fail("Delete Bank Account :- For InValid BankAccountId, Bank Account Update Allowed");
        }
        catch(BankAccountNotFoundException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Delete Bank Account :- For InValid BankAccount Id, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void getBankAccountByAccountIdTest(UUID userId, BankAccountDTO bankAccountDTO)
    {
        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);
        when(bankAccountRepository.findById(bankAccountDTO.getBankAccountId())).thenReturn(Optional.of(bankAccount));

        BankAccountDTO bankAccountResponseDTO = bankAccountService.getBankAccountByAccountId(bankAccountDTO.getBankAccountId());

        assertEquals(bankAccountResponseDTO.getAccountNumber(),bankAccountDTO.getAccountNumber());
        assertEquals(bankAccountResponseDTO.getAccountHolderName(),bankAccountDTO.getAccountHolderName());
        assertEquals(bankAccountResponseDTO.getIfscCode(),bankAccountDTO.getIfscCode());
        assertEquals(bankAccountResponseDTO.getBankName(),bankAccountDTO.getBankName());
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void getBankAccountByAccountIdTestInValidBankAccountId(UUID userId, BankAccountDTO bankAccountDTO)
    {
        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);
        when(bankAccountRepository.findById(bankAccountDTO.getBankAccountId())).thenReturn(Optional.ofNullable(null));

        try {
            BankAccountDTO bankAccountResponseDTO = bankAccountService.getBankAccountByAccountId(bankAccountDTO.getBankAccountId());
            fail("Delete Bank Account :- For Invalid BankAccount Exception Not Thrown");
        }
        catch(BankAccountNotFoundException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Delete Bank Account :- For InValid BankAccount Id, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void bankAccountExistTest(UUID userId, BankAccountDTO bankAccountDTO)
    {
        BankAccount bankAccount = bankAccountMapper.requestDtoTo(bankAccountDTO);
        when(bankAccountRepository.findById(bankAccountDTO.getBankAccountId())).thenReturn(Optional.ofNullable(null));

        try {
            bankAccountService.bankAccountExist(userId,bankAccountDTO.getBankAccountId());
            fail("Bank Account Exist :- For Invalid BankAccount Exception Not Thrown");
        }
        catch(BankAccountNotFoundException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            fail("Find Bank Account :- For InValid BankAccount Id, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }
    }

    @ParameterizedTest
    @ArgumentsSource(BankAccountProvider.class)
    public void getAllBankAccountByUserId(UUID userId, BankAccountDTO bankAccountDTO)
    {

        BankAccount bankAccount1 = bankAccountMapper.requestDtoTo(bankAccountDTO);
        BankAccount bankAccount2 = bankAccountMapper.requestDtoTo(bankAccountDTO);
        bankAccount1.setBankAccountId(UUID.randomUUID());
        bankAccount2.setBankAccountId(UUID.randomUUID());
        List<BankAccount> bankAccounts = Stream.of(bankAccount1, bankAccount2).toList();

        when(bankAccountRepository.findByUserUserId(userId)).thenReturn(bankAccounts);

        List<BankAccountDTO> bankAccountsResponse = bankAccountService.getAllBankAccountByUserId(userId);

        for(int i=0;i<bankAccounts.size();i++)
        {
            assertEquals(bankAccounts.get(i).getBankAccountId(),bankAccountsResponse.get(i).getBankAccountId());
        }

    }




}
