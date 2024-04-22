package com.digitallending.breservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class BRESubParamWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subParamWeightId;

    @ManyToOne
    @JoinColumn(name = "subParamTypeId", referencedColumnName = "subParamTypeId")
    private BRESubParamType breSubParamType;

    private Integer weight;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subParamWeightId", referencedColumnName = "subParamWeightId")
    private List<BRESubParamValue> breSubParamValueList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subParamWeightId", referencedColumnName = "subParamWeightId")
    private List<BRESubParamRange> breSubParamRangeList = new ArrayList<>();
}
