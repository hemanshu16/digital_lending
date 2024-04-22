package com.digitallending.breservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class BRESubParamTypeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID breSubParamTypeValueId;
    private String value;
}
