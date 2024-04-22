package com.digitallending.breservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class BREParamType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paramTypeId;
    private String paramName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paramTypeId", referencedColumnName = "paramTypeId")
    private List<BRESubParamType> breSubParamTypeList;
}
