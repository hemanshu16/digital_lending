package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.LoanBREParamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanBREParamTypeRepository extends JpaRepository<LoanBREParamType, UUID> {
    List<LoanBREParamType> findByLoanTypeId(UUID loanTypeId);
}
