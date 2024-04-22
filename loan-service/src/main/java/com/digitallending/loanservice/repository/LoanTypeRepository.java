package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, UUID> {
}
