package com.digitallending.breservice.repository;

import com.digitallending.breservice.model.entity.BREParamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BREParamTypeRepository extends JpaRepository<BREParamType, UUID> {
}
