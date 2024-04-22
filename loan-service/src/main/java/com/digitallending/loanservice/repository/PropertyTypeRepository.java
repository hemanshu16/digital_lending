package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, UUID> {
}
