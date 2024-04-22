package com.digitallending.loanservice.model.entity;

import com.digitallending.loanservice.model.enums.LoanApplicationStage;
import com.digitallending.loanservice.model.enums.LoanApplicationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanApplicationId;
    private UUID userId;
    private String userName;
    private Long monthlyIncome; // in Rs.
    private Long businessExperience; // in month
    private String loanApplicationName;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "loanTypeId", referencedColumnName = "loanTypeId")
    private LoanType loanType;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="signedDocumentId", referencedColumnName = "signedDocumentId")
    private SignedDocument signedDocument;
    private Long amount; // in Rs.
    private Long tenure; // in month

    @Column(columnDefinition = "TEXT")
    private String statementOfPurpose;

    private Float salesGrowth; // in percentage
    private Float profitGrowth; // in percentage
    private Float netProfit; // in percentage
    private Integer cibilScore;
    private Integer cibilRank;

    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name = "loanApplicationId",referencedColumnName = "propertyDetailsId")
    private PropertyDetails propertyDetails;

    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus loanApplicationStatus;
    @Enumerated(EnumType.STRING)
    private LoanApplicationStage loanApplicationStage;
    @Column(columnDefinition = "BIGINT")
    private Long BREFilterTime;
    @PrePersist
    public void prePersist() {
        if (this.BREFilterTime == null) {
            this.BREFilterTime = 0L;
        }
    }
}
