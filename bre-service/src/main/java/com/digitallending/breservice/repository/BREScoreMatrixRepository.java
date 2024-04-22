package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.BREScoreMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BREScoreMatrixRepository extends JpaRepository<BREScoreMatrix, UUID> {
    Boolean existsByLoanProductId(UUID loanProductId);
    BREScoreMatrix findByLoanProductIdAndScoreFromLessThanEqualAndScoreToGreaterThanEqual(UUID loanProductId, Integer paramScore1,Integer paramScore2);
}
