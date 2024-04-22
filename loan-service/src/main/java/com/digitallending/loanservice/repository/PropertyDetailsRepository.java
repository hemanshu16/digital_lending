package com.digitallending.loanservice.repository;

import com.digitallending.loanservice.model.entity.PropertyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyDetailsRepository extends JpaRepository<PropertyDetails, UUID> {
}
