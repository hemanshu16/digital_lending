package com.digitallending.breservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;


@Data
@Entity
public class LoanBREParamType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanBreParamTypeId;
    private UUID loanTypeId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paramTypeId", referencedColumnName = "paramTypeId")
    private BREParamType breParamType;
}
