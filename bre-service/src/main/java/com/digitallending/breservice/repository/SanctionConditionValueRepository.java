package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.SanctionConditionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanctionConditionValueRepository extends JpaRepository<SanctionConditionValue, UUID> {
    Boolean existsByLoanProductId(UUID loanProductId);
    List<SanctionConditionValue> findByLoanProductId(UUID loanProductId);
}
