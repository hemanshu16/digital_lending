package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.SanctionConditionRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanctionConditionRangeRepository extends JpaRepository<SanctionConditionRange, UUID> {
    Boolean existsByLoanProductId(UUID loanProductId);
    List<SanctionConditionRange> findByLoanProductId(UUID loanProductId);
}
