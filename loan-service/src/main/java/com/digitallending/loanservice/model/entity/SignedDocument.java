package com.digitallending.loanservice.model.entity;

import com.digitallending.loanservice.model.enums.SignedDocumentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID signedDocumentId;
    @Enumerated(EnumType.STRING)
    private SignedDocumentStatus status;
    @Lob
    private byte[] documentContent;

    @OneToOne(mappedBy = "signedDocument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanApplication loanApplication;
}
