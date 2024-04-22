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
public class BRESubParamType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subParamTypeId;
    private String subParamName;
    private Boolean isRange;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subParamTypeId", referencedColumnName = "subParamTypeId")
    private List<BRESubParamTypeValue> breSubParamTypeValueList;

}
