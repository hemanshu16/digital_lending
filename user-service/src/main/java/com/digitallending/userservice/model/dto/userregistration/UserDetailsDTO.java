package com.digitallending.userservice.model.dto.userregistration;

import com.digitallending.userservice.enums.Role;
import com.digitallending.userservice.enums.UserOnBoardingStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
@Builder
public class UserDetailsDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNo;
    private Date dob;
    private Role role;
    private UserOnBoardingStatus onBoardingStatus;
}
