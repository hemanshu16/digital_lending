package com.digitallending.userservice.service;

import com.digitallending.userservice.enums.UserOnBoardingStatus;
import com.digitallending.userservice.exception.EmptyDocumentException;
import com.digitallending.userservice.exception.PreviousStepsNotDoneException;
import com.digitallending.userservice.exception.UpdateException;
import com.digitallending.userservice.model.dto.userdetails.MsmeUserDocumentDTO;
import com.digitallending.userservice.model.dto.userdetails.UserDocumentDetailsDTO;
import com.digitallending.userservice.model.entity.UserDetails;
import com.digitallending.userservice.model.entity.msmeuser.MsmeUserDocument;
import com.digitallending.userservice.model.entity.msmeuser.UserDocumentType;
import com.digitallending.userservice.model.mapper.MsmeUserDocumentMapperImpl;
import com.digitallending.userservice.providers.msmeuserdetails.MsmeUserDocumentProvider;
import com.digitallending.userservice.repository.MsmeUserDocumentRepository;
import com.digitallending.userservice.repository.MsmeUserDocumentTypeRepository;
import com.digitallending.userservice.service.def.UserDetailsService;
import com.digitallending.userservice.service.impl.MsmeUserDocumentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MsmeUserDocumentServiceTest {

    @InjectMocks
    private MsmeUserDocumentServiceImpl msmeUserDocumentService;

    @Mock
    private UserDetailsService userDetailService;

    @Mock
    private MsmeUserDocumentTypeRepository msmeUserDocumentTypeRepository;

    @Mock
    private MsmeUserDocumentRepository msmeUserDocumentRepository;

    @InjectMocks
    private MsmeUserDocumentMapperImpl msmeUserDocumentMapper;


    private UserDetails userDetails = UserDetails.builder()
            .userId(UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f25eb"))
            .onBoardingStatus(UserOnBoardingStatus.USER_DETAILS)
            .build();

    private UUID maritalStatusId = UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2001");
    private UUID categoryId = UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2002");
    private UUID educationQualificationId = UUID.fromString("c27d54a6-a2fc-4146-8dc3-35cfa69f2003");
    private UserDocumentType maritalStatus = UserDocumentType
            .builder()
            .documentType("Marital Status")
            .documentTypeId(maritalStatusId)
            .build();

    private UserDocumentType category = UserDocumentType
            .builder()
            .documentType("Category Document")
            .documentTypeId(categoryId)
            .build();

    private UserDocumentType educationQualification = UserDocumentType
            .builder()
            .documentType("Education Qualification")
            .documentTypeId(educationQualificationId)
            .build();

    @BeforeEach
    public void init() {

        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        ReflectionTestUtils.setField(msmeUserDocumentService, "msmeUserDocumentMapper", msmeUserDocumentMapper);
    }
    @ParameterizedTest
    @ArgumentsSource(MsmeUserDocumentProvider.class)
    public void updateDocumentTest(UUID userId, UUID documentTypeId, MultipartFile document) throws IOException {

        when(userDetailService.getUserDetails(userId)).thenReturn(userDetails);
        when(msmeUserDocumentTypeRepository.findById(maritalStatusId)).thenReturn(Optional.of(maritalStatus));
        when(msmeUserDocumentTypeRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(msmeUserDocumentTypeRepository.findById(educationQualificationId)).thenReturn(Optional.of(educationQualification));

        MsmeUserDocument msmeUserDocument;
        if (documentTypeId.equals(maritalStatusId)) {
            msmeUserDocument = MsmeUserDocument.builder()
                    .user(userDetails)
                    .documentType(maritalStatus)
                    .build();

        } else if (documentTypeId.equals(categoryId)) {
            msmeUserDocument = MsmeUserDocument.builder()
                    .user(userDetails)
                    .documentType(category)
                    .build();

        } else {
            msmeUserDocument = MsmeUserDocument.builder()
                    .user(userDetails)
                    .documentType(educationQualification)
                    .build();
        }

        when(msmeUserDocumentRepository.findByUserUserIdAndDocumentTypeDocumentTypeId(userId, documentTypeId)).thenReturn(msmeUserDocument);

        when(msmeUserDocumentRepository.save(any(MsmeUserDocument.class)))
                .thenAnswer(new Answer<MsmeUserDocument>() {
                    @Override
                    public MsmeUserDocument answer(InvocationOnMock invocation) throws Throwable {
                        MsmeUserDocument msmeUserDocument = invocation.getArgument(0);
                        msmeUserDocument.setMsmeUserDocumentId(UUID.randomUUID());
                        return msmeUserDocument;
                    }
                });

        when(msmeUserDocumentRepository.countByUserUserId(userId)).thenReturn(3);

        when(msmeUserDocumentTypeRepository.count()).thenReturn(3L);

        ArgumentCaptor<UserDetails> argument = ArgumentCaptor.forClass(UserDetails.class);

        MsmeUserDocumentDTO msmeUserDocumentDTO = msmeUserDocumentService.updateDocument(userId, documentTypeId, document);

        verify(userDetailService,times(1)).updateUserDetails(argument.capture());

        assertEquals(UserOnBoardingStatus.USER_DOC,argument.getValue().getOnBoardingStatus());

        assertEquals(msmeUserDocumentDTO.getDocumentType().getDocumentTypeId(),documentTypeId);

    }

    @ParameterizedTest
    @ArgumentsSource(MsmeUserDocumentProvider.class)
    public void updateDocumentTestNotValidUserStatus(UUID userId, UUID documentTypeId, MultipartFile document) throws IOException {

        when(msmeUserDocumentTypeRepository.findById(maritalStatusId)).thenReturn(Optional.of(maritalStatus));
        when(msmeUserDocumentTypeRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(msmeUserDocumentTypeRepository.findById(educationQualificationId)).thenReturn(Optional.of(educationQualification));

        UserOnBoardingStatus[] statusValues = UserOnBoardingStatus.values();

        for(int i=0;i<UserOnBoardingStatus.USER_DETAILS.ordinal();i++)
        {
            userDetails.setOnBoardingStatus(statusValues[i]);
            when(userDetailService.getUserDetails(userId)).thenReturn(userDetails);
            try{
                msmeUserDocumentService.updateDocument(userId, documentTypeId, document);
            }
            catch(PreviousStepsNotDoneException exception)
            {
                assertTrue(true);
            }
            catch(Exception exception)
            {
                fail("Invalid User Status but Document update Allowed");
            }
        }

        for(int i=UserOnBoardingStatus.ON_HOLD.ordinal()+1;i<statusValues.length;i++)
        {
            userDetails.setOnBoardingStatus(statusValues[i]);
            when(userDetailService.getUserDetails(userId)).thenReturn(userDetails);
            try{
                msmeUserDocumentService.updateDocument(userId, documentTypeId, document);
            }
            catch(UpdateException exception)
            {
                assertTrue(true);
            }
            catch(Exception exception)
            {
                fail("Invalid User Status but Document update Allowed");
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(MsmeUserDocumentProvider.class)
    public void newDocumentTest(UUID userId, UUID documentTypeId, MultipartFile document) throws IOException {

        when(userDetailService.getUserDetails(userId)).thenReturn(userDetails);
        when(msmeUserDocumentTypeRepository.findById(maritalStatusId)).thenReturn(Optional.of(maritalStatus));
        when(msmeUserDocumentTypeRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(msmeUserDocumentTypeRepository.findById(educationQualificationId)).thenReturn(Optional.of(educationQualification));


        when(msmeUserDocumentRepository.findByUserUserIdAndDocumentTypeDocumentTypeId(userId, documentTypeId)).thenReturn(null);

        when(msmeUserDocumentRepository.save(any(MsmeUserDocument.class)))
                .thenAnswer(new Answer<MsmeUserDocument>() {
                    @Override
                    public MsmeUserDocument answer(InvocationOnMock invocation) throws Throwable {
                        MsmeUserDocument msmeUserDocument = invocation.getArgument(0);
                        msmeUserDocument.setMsmeUserDocumentId(UUID.randomUUID());
                        return msmeUserDocument;
                    }
                });

        when(msmeUserDocumentRepository.countByUserUserId(userId)).thenReturn(3);
        when(msmeUserDocumentTypeRepository.count()).thenReturn(3L);

        ArgumentCaptor<UserDetails> argument = ArgumentCaptor.forClass(UserDetails.class);

        MsmeUserDocumentDTO msmeUserDocumentDTO = msmeUserDocumentService.updateDocument(userId, documentTypeId, document);

        verify(userDetailService,times(1)).updateUserDetails(argument.capture());

        assertEquals(UserOnBoardingStatus.USER_DOC,argument.getValue().getOnBoardingStatus());

        assertEquals(msmeUserDocumentDTO.getDocumentType().getDocumentTypeId(),documentTypeId);
    }

    @ParameterizedTest
    @ArgumentsSource(MsmeUserDocumentProvider.class)
    public void updateDocumentTestEmptyFile(UUID userId, UUID documentTypeId, MultipartFile document) throws IOException {

        when(userDetailService.getUserDetails(userId)).thenReturn(userDetails);
        when(msmeUserDocumentTypeRepository.findById(maritalStatusId)).thenReturn(Optional.of(maritalStatus));
        when(msmeUserDocumentTypeRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(msmeUserDocumentTypeRepository.findById(educationQualificationId)).thenReturn(Optional.of(educationQualification));


        when(msmeUserDocumentRepository.findByUserUserIdAndDocumentTypeDocumentTypeId(userId, documentTypeId)).thenReturn(null);

        document = new MockMultipartFile("Name",new byte[]{});
        try {
            MsmeUserDocumentDTO msmeUserDocumentDTO = msmeUserDocumentService.updateDocument(userId, documentTypeId, document);
        }
        catch (EmptyDocumentException exception)
        {
            assertTrue(true);
        }
        catch(Exception exception)
        {
            Assertions.fail("Empty Document Allowed");
        }
    }

    @Test
    public void getAllDocumentByUserId()
    {
        UUID userId = UUID.randomUUID();



        Set<UserDocumentType> documentTypes = new HashSet<>(List.of(category,educationQualification,maritalStatus));

        MsmeUserDocument msmeUserCategoryDocument = MsmeUserDocument
                .builder()
                .msmeUserDocumentId(UUID.randomUUID())
                .documentContent(new byte[]{1,1})
                .documentType(category)
                .build();

        when(msmeUserDocumentRepository.findByUserUserId(userId)).thenReturn(List.of(msmeUserCategoryDocument));

        when(msmeUserDocumentTypeRepository.findAll()).thenReturn(documentTypes.stream().toList());

        UserDocumentDetailsDTO documents = msmeUserDocumentService.getAllDocumentByUserId(userId);

        assertEquals(1,documents.getSubmittedDocuments().size());

        assertEquals(msmeUserCategoryDocument.getDocumentType(),documents.getSubmittedDocuments().getFirst().getDocumentType());

        assertEquals(2,documents.getRemainingDocuments().size());

        for(UserDocumentType documentType : documents.getRemainingDocuments())
        {
           assertTrue(documentTypes.contains(documentType));
           documentTypes.remove(documentType);
        }

        assertEquals(1,documentTypes.size());

    }
}
