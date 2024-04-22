package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.BREParamWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BREParamWeightRepository extends JpaRepository<BREParamWeight, UUID> {
    List<BREParamWeight> findByLoanProductIdAndWeightNot(UUID loanProductId, int weight);
}
