package com.digitallending.loanservice.model.entity;

import com.digitallending.loanservice.model.enums.PropertyTypeName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class PropertyType {
    @Id
    private UUID propertyTypeId;
    @Enumerated(EnumType.STRING)
    private PropertyTypeName propertyTypeName;
}
