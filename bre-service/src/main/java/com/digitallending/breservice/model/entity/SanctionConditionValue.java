package com.digitallending.breservice.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SanctionConditionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID loanProductId;
    @ManyToOne
    @JoinColumn(name = "breSubParamTypeValueId",referencedColumnName = "breSubParamTypeValueId")
    private BRESubParamTypeValue breSubParamTypeValue;
    @ManyToOne
    @JoinColumn(name="subParamTypeId",referencedColumnName = "subParamTypeId")
    private BRESubParamType breSubParamType;
}
