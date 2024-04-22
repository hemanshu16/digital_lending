package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.BRESubParamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BRESubParamTypeRepository extends JpaRepository<BRESubParamType, UUID> {
}
