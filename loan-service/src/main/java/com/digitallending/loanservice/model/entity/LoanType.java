package com.digitallending.loanservice.model.entity;

import com.digitallending.loanservice.model.enums.LoanTypeName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class LoanType {
    @Id
    private UUID loanTypeId;
    @Enumerated(EnumType.STRING)
    private LoanTypeName loanTypeName;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "documentTypeByLoanType",
            joinColumns = @JoinColumn(name = "loanTypeId", referencedColumnName = "loanTypeId"),
            inverseJoinColumns = @JoinColumn(name = "documentTypeId", referencedColumnName = "documentTypeId"))
    private List<DocumentType> documentTypeList;
}
