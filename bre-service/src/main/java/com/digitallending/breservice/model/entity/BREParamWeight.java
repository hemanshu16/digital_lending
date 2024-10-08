package com.digitallending.breservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"loanProductId", "paramTypeId"}))
public class BREParamWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paramWeightId;
    private UUID loanProductId;
    @ManyToOne
    @JoinColumn(name = "paramTypeId", referencedColumnName = "paramTypeId")
    private BREParamType paramType;
    private Integer weight;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paramWeightId", referencedColumnName = "paramWeightId")
    private List<BRESubParamWeight> breSubParamWeightsList;
}
