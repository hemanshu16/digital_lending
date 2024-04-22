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

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SanctionConditionRange {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID loanProductId;
    private BigDecimal min;
    private BigDecimal max;
    @ManyToOne
    @JoinColumn(name="subParamTypeId", referencedColumnName = "subParamTypeId")
    private BRESubParamType breSubParamType;
}
