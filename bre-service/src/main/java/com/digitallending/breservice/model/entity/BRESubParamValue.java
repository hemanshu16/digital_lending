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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class BRESubParamValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID valueId;
    @ManyToOne
    @JoinColumn(name = "breSubParamTypeValueId",referencedColumnName = "breSubParamTypeValueId")
    private BRESubParamTypeValue breSubParamTypeValue;
    private Integer score;
}
