package com.digitallending.loanservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanProductId;
    private UUID userId;
    private String userName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanTypeId", referencedColumnName = "loanTypeId")
    private LoanType loanType;
    private Long maxAmount;
    private Long maxTenure;
    private String loanProductName;
    private String loanProductDescription;
    @Column(columnDefinition = "BIGINT")
    private Long creationTime;

    @PrePersist
    public void prePersist() {
        if (this.creationTime == null) {
            this.creationTime = Instant.now().getEpochSecond();
        }
    }
}
