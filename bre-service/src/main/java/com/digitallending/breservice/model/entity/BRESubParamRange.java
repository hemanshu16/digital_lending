package com.digitallending.breservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class BRESubParamRange {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID rangeId;
    private BigDecimal min;
    private BigDecimal max;
    private Integer score;
}
