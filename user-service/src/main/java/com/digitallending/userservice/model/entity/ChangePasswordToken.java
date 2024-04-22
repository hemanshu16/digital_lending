package com.digitallending.userservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "change_password_token")
public class ChangePasswordToken {

    @Id
    private UUID token;
    private String email;
}
