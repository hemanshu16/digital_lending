package com.digitallending.loanservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class PropertyDetails {
    //    taken from loanApplication
    @Id
    private UUID propertyDetailsId;
    private Date propertyVintage;
    private Long propertyValuation; // in Rs.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "propertyTypeId", name = "propertyTypeId")
    private PropertyType propertyType;
}
