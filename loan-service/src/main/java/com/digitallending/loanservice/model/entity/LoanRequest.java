package com.digitallending.loanservice.model.entity;

import com.digitallending.loanservice.model.enums.LoanRequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loanRequestId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "loanProductId", name = "loanProductId")
    private LoanProduct loanProduct;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "loanApplicationId", name = "loanApplicationId")
    private LoanApplication loanApplication;
    private Double interestRate;
    @Enumerated(EnumType.STRING)
    private LoanRequestStatus status;
    private UUID providerAccountId;
    private UUID receiverAccountId;
    private Integer score;
}
