package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.AttributeValueNotFoundException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.exception.UserNotFoundException;
import com.digitallending.userservice.model.dto.admin.UserPaginationResponseDTO;
import com.digitallending.userservice.model.dto.business.BusinessDetailsBREDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsRequestDTO;
import com.digitallending.userservice.model.dto.msmeuserdetails.MsmeUserDetailsResponseDTO;
import com.digitallending.userservice.model.dto.userdetails.UserBRERequestDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttribute;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDetailsAttributeValue;
import com.digitallending.userservice.model.mapper.MsmeUserDetailsMapperImpl;
import com.digitallending.userservice.providers.msmeuserdetails.MsmeUserDetailaRequestDTONotValidAttributeProvider;
import com.digitallending.userservice.providers.msmeuserdetails.MsmeUserDetailsRequestDTOProvider;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeRepository;
import com.digitallending.userservice.repository.MsmeUserDetailsAttributeValueRepository;
import com.digitallending.userservice.repository.MsmeUserDetailsRepository;
import com.digitallending.userservice.service.def.BusinessDetailsService;
import com.digitallending.userservice.service.impl.MsmeUserDetailsServiceImpl;
import com.digitallending.userservice.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.digitallending.userservice.utils.DateUtil.getMonthDifference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

public class MsmeUserDetailsServiceTest {

    @InjectMocks
    private MsmeUserDetailsServiceImpl msmeUserDetailsService;

    @Mock
    private MsmeUserDetailsRepository msmeUserDetailsRepository;

    @Mock
    private MsmeUserDetailsAttributeRepository msmeUserDetailsAttributeRepository;

    @Mock
    private MsmeUserDetailsAttributeValueRepository msmeUserDetailsAttributeValueRepository;
    @InjectMocks
    private MsmeUserDetailsMapperImpl msmeUserDetailsMapper;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BusinessDetailsService businessDetailsService;

    private MsmeUserDetailsAttribute education = MsmeUserDetailsAttribute
            .builder()
            .attributeName("educationalQualification")
            .build();
    private MsmeUserDetailsAttribute gender = MsmeUserDetailsAttribute
            .builder()
            .attributeName("gender")
            .build();
    private MsmeUserDetailsAttribute category = MsmeUserDetailsAttribute
            .builder()
            .attributeName("category")
            .build();
    private MsmeUserDetailsAttribute maritalStatus = MsmeUserDetailsAttribute
            .builder()
            .attributeName("maritalStatus")
            .build();
    private MsmeUserDetailsAttributeValue male = MsmeUserDetailsAttributeValue.builder().value("MALE").msmeUserDetailsAttribute(gender).build();
    private MsmeUserDetailsAttributeValue others = MsmeUserDetailsAttributeValue.builder().value("OTHERS").msmeUserDetailsAttribute(category).build();
    private MsmeUserDetailsAttributeValue married = MsmeUserDetailsAttributeValue.builder().value("MARRIED").msmeUserDetailsAttribute(maritalStatus).build();
    private MsmeUserDetailsAttributeValue phd = MsmeUserDetailsAttributeValue.builder().value("PHD").msmeUserDetailsAttribute(education).build();

    private MsmeUserDetails msmeUserDetails = MsmeUserDetails
            .builder()
            .userId(UUID.randomUUID())
            .gender(male)
            .category(others)
            .maritalStatus(married)
            .educationalQualification(phd).build();

    @BeforeEach
    public void init() {

        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        ReflectionTestUtils.setField(msmeUserDetailsService, "msmeUserDetailsMapper", msmeUserDetailsMapper);
    }

    @ParameterizedTest
    @ArgumentsSource(MsmeUserDetailsRequestDTOProvider.class)
    public void saveMsmeUserDetailsTest(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getGender())).thenReturn(Optional.of(male));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getCategory())).thenReturn(Optional.of(others));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getMaritalStatus())).thenReturn(Optional.of(married));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getEducationalQualification())).thenReturn(Optional.of(phd));

        when(msmeUserDetailsRepository.save(msmeUserDetails)).thenReturn(msmeUserDetails);

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PAN);

        MsmeUserDetailsResponseDTO msmeUserDetailsResponseDTO = MsmeUserDetailsResponseDTO
                .builder()
                .category("OTHERS")
                .educationalQualification("PHD")
                .gender("MALE")
                .maritalStatus("MARRIED")
                .build();

        when(msmeUserDetailsRepository.save(ArgumentMatchers.any(MsmeUserDetails.class)))
                .thenAnswer(new Answer<MsmeUserDetails>() {
                    @Override
                    public MsmeUserDetails answer(InvocationOnMock invocation) throws Throwable {
                        MsmeUserDetails msmeUserDetails = invocation.getArgument(0);
                        msmeUserDetails.setUserId(UUID.randomUUID());
                        return msmeUserDetails;
                    }
                });

        MsmeUserDetailsResponseDTO msmeUserDetailsResponseDTO1 = msmeUserDetailsService.saveMsmeUserDetails(userId, msmeUserDetailsRequestDTO);

        assertEquals(msmeUserDetailsResponseDTO1.getEducationalQualification(), "PHD");
        assertEquals(msmeUserDetailsResponseDTO1.getMaritalStatus(), "MARRIED");
        assertEquals(msmeUserDetailsResponseDTO1.getCategory(), "OTHERS");
        assertEquals(msmeUserDetailsResponseDTO1.getGender(), "MALE");


    }

    /*
     *  If Previous Status is not VERIFY_PAN then not able to save details
     */
    @ParameterizedTest
    @ArgumentsSource(MsmeUserDetailsRequestDTOProvider.class)
    public void saveMsmeUserDetailsTestWrongPreviousStatus(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.BANK_DETAILS);

        try {
            msmeUserDetailsService.saveMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
            fail("Wrong User Status But save Msme User details Allowed");
        } catch (PreviousStepsNotDoneException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            assertFalse(true);
        }

    }

    /*
     * Not Valid ArgumentValueId Provided
     */

    @ParameterizedTest
    @ArgumentsSource(MsmeUserDetailaRequestDTONotValidAttributeProvider.class)
    public void saveMsmeUserDetailsTestWrongAttributeValueId(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        when(userDetailsService.getUserStatus(userId)).thenReturn(UserOnBoardingStatus.VERIFY_PAN);

        try {
            msmeUserDetailsService.saveMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
            fail("Wrong User Status But save Msme User Details Allowed");
        } catch (AttributeValueNotFoundException exception) {
            assertTrue(true);
        } catch (Exception exception) {
            assertTrue(false);
        }

    }

    /*
    *  Update User Details
    *
    */
    @ParameterizedTest
    @ArgumentsSource(MsmeUserDetailsRequestDTOProvider.class)
    public void updateMsmeUserDetailsTest(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getGender())).thenReturn(Optional.of(male));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getCategory())).thenReturn(Optional.of(others));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getMaritalStatus())).thenReturn(Optional.of(married));
        when(msmeUserDetailsAttributeValueRepository.findById(msmeUserDetailsRequestDTO.getEducationalQualification())).thenReturn(Optional.of(phd));

        MsmeUserDetails msmeUserDetails = MsmeUserDetails
                .builder()
                .gender(male)
                .category(others)
                .maritalStatus(married)
                .educationalQualification(phd).build();

        when(msmeUserDetailsRepository.save(ArgumentMatchers.any(MsmeUserDetails.class)))
                .thenAnswer(new Answer<MsmeUserDetails>() {
                    @Override
                    public MsmeUserDetails answer(InvocationOnMock invocation) throws Throwable {
                        MsmeUserDetails msmeUserDetails = invocation.getArgument(0);
                        msmeUserDetails.setUserId(UUID.randomUUID());
                        return msmeUserDetails;
                    }
                });

        MsmeUserDetailsResponseDTO msmeUserDetailsResponseDTO = MsmeUserDetailsResponseDTO
                .builder()
                .category("OTHERS")
                .educationalQualification("PHD")
                .gender("MALE")
                .maritalStatus("MARRIED")
                .build();

        UserOnBoardingStatus[] values = UserOnBoardingStatus.values();
        int start = UserOnBoardingStatus.USER_DOC.ordinal();
        int end = UserOnBoardingStatus.ON_HOLD.ordinal();
        // For status USER_DOC to ON_HOLD User can update details
        for (int i = start; i <= end; i++) {
            when(userDetailsService.getUserStatus(userId)).thenReturn(values[i]);

            MsmeUserDetailsResponseDTO msmeUserDetailsResponseDTO1 = msmeUserDetailsService.updateMsmeUserDetails(userId, msmeUserDetailsRequestDTO);

            assertEquals(msmeUserDetailsResponseDTO1.getEducationalQualification(), "PHD");
            assertEquals(msmeUserDetailsResponseDTO1.getMaritalStatus(), "MARRIED");
            assertEquals(msmeUserDetailsResponseDTO1.getCategory(), "OTHERS");
            assertEquals(msmeUserDetailsResponseDTO1.getGender(), "MALE");

        }
    }

    /*
     *  For InValid User Status Update Should Not allowed
     */
    @ParameterizedTest
    @ArgumentsSource(MsmeUserDetailsRequestDTOProvider.class)
    public void updateMsmeUserDetailsTestWrongPreviousStatus(UUID userId, MsmeUserDetailsRequestDTO msmeUserDetailsRequestDTO) {

        UserOnBoardingStatus[] values = UserOnBoardingStatus.values();
        int start = UserOnBoardingStatus.USER_DETAILS.ordinal();
        int end = UserOnBoardingStatus.ON_HOLD.ordinal();
        for(int i=0;i<start;i++) {
            when(userDetailsService.getUserStatus(userId)).thenReturn(values[i]);

            try {
                msmeUserDetailsService.updateMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
            } catch (UpdateException exception) {
                assertTrue(true);
            } catch (Exception exception) {
                fail("Update MSME Details, Wrong User Status still update allowed");
            }
        }
        for(int i=end+1;i<values.length;i++) {
            when(userDetailsService.getUserStatus(userId)).thenReturn(values[i]);

            try {
                msmeUserDetailsService.updateMsmeUserDetails(userId, msmeUserDetailsRequestDTO);
            } catch (UpdateException exception) {
                assertTrue(true);
            } catch (Exception exception) {
                fail("Update MSME Details, Wrong User Status still update allowed");
            }
        }

    }

    @Test
    public void getMsmeUserDetails()
    {
        when(msmeUserDetailsRepository.findById(any(UUID.class))).thenReturn(Optional.of(msmeUserDetails));
        MsmeUserDetailsResponseDTO msmeUserDetailsResponse = msmeUserDetailsService.getMsmeUserDetails(UUID.randomUUID());
        assertEquals(msmeUserDetailsResponse.getEducationalQualification(), "PHD");
        assertEquals(msmeUserDetailsResponse.getMaritalStatus(), "MARRIED");
        assertEquals(msmeUserDetailsResponse.getCategory(), "OTHERS");
        assertEquals(msmeUserDetailsResponse.getGender(), "MALE");

        when(msmeUserDetailsRepository.findById(any(UUID.class))).thenThrow(new UserNotFoundException("User Not Exist"));
        try{
            msmeUserDetailsService.getMsmeUserDetails(UUID.randomUUID());
        }
        catch(UserNotFoundException exception){
            assertTrue(true);
        }
        catch (Exception exception)
        {
            fail("For Invalid UserId Exception Not Thrown");
        }

    }

    @Test
    void getMsmeUserAllDetailsTest()
    {
        UUID userId = UUID.randomUUID();

        when(msmeUserDetailsRepository.findById(userId)).thenReturn(Optional.of(msmeUserDetails));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dateOfBirth = calendar.getTime();
        calendar.set(Calendar.YEAR,2010);
        Date businessExperience = calendar.getTime();

        calendar.set(Calendar.YEAR,2012);
        Date businessVintage = calendar.getTime();

        Integer businessVintageMonths = getMonthDifference(new Date(), businessVintage);
        Integer businessExperienceMonths = getMonthDifference(new Date(), businessExperience);
        Integer age = getMonthDifference(new Date(),dateOfBirth);

        when(userDetailsService.getDobByUserId(userId)).thenReturn(dateOfBirth);

        BusinessDetailsBREDTO businessDetailsBREDTO = BusinessDetailsBREDTO
                .builder()
                .businessExperience(businessExperience)
                .registrationDate(businessVintage)
                .build();


        when(businessDetailsService.getBREValues(userId)).thenReturn(businessDetailsBREDTO);

        UserBRERequestDTO msmeUserAllDetails = msmeUserDetailsService.getMsmeUserAllDetails(userId);

        assertEquals(msmeUserDetails.getCategory().getValue(),msmeUserAllDetails.getCategory());

        assertEquals(msmeUserDetails.getGender().getValue(),msmeUserAllDetails.getGender());

        assertEquals(msmeUserDetails.getEducationalQualification().getValue(),msmeUserAllDetails.getEducationalQualification());

        assertEquals(msmeUserDetails.getMaritalStatus().getValue(),msmeUserAllDetails.getMaritalStatus());

        assertEquals(businessVintageMonths,msmeUserAllDetails.getBusinessVintage());

        assertEquals(businessExperienceMonths,msmeUserAllDetails.getBusinessExperience());

        assertEquals(age,msmeUserAllDetails.getAge());


    }

    @Test
    void getMsmeUserAllDetailsTestWrongUserId()
    {
        when(msmeUserDetailsRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        try{
            msmeUserDetailsService.getMsmeUserDetails(UUID.randomUUID());
            fail("Get MSME User All Details :- For Wrong User Id Exception Not Thrown");
        }
        catch(UserNotFoundException exception)
        {
            assertTrue(true);
        }
        catch (Exception exception)
        {
            fail("Get MSME User All Details :- For InValid User Id, Not Getting Expected Exception"
                    + "\nException Name :- " + exception.getClass().getName()
                    +"\n Message " + exception.getMessage());
        }
    }

    @Test
    void listOfUserIdByEducationQualification()
    {
        when(msmeUserDetailsAttributeValueRepository.findByValue("PHD")).thenReturn(phd);
        Page<MsmeUserDetails> pageResponse = new PageImpl<>(List.of(msmeUserDetails,msmeUserDetails), PageRequest.of(0, 2), 4);
        when(msmeUserDetailsRepository.findByEducationalQualification(any(MsmeUserDetailsAttributeValue.class),any(Pageable.class)))
                .thenReturn(pageResponse);

        UserPaginationResponseDTO paginationResponseDTO = msmeUserDetailsService.listOfUserIdByEducationQualification("PHD", 0, 2);

        assertEquals(4,paginationResponseDTO.getTotalElements());
        assertEquals(2,paginationResponseDTO.getTotalPages());
        assertEquals(0,paginationResponseDTO.getPageNo());
        assertEquals(2,paginationResponseDTO.getPageSize());
        assertEquals(false,paginationResponseDTO.isLast());

    }

    @Test
    void listOfUserIdByEducationQualificationTestWrongAttributeValue()
    {
        when(msmeUserDetailsAttributeValueRepository.findByValue("PHD")).thenReturn(null);

       try {
           msmeUserDetailsService.listOfUserIdByEducationQualification("PHD", 0, 2);
           fail("List of UserId by Education Qualification :- For Wrong Attribute Value Exception Not Thrown");
       }
       catch (AttributeValueNotFoundException exception)
       {
           assertTrue(true);
       }
       catch(Exception exception)
       {
           fail("List of UserId by Education Qualification :- For Wrong Attribute Value, Not Getting Expected Exception"
                   + "\nException Name :- " + exception.getClass().getName()
                   +"\n Message " + exception.getMessage());
       }

    }

}
