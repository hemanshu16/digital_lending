package com.digitallending.loanservice.service.def;

import com.digitallending.loanservice.model.entity.PropertyType;

import java.util.List;
import java.util.UUID;

public interface PropertyTypeService {
    PropertyType getPropertyTypeById(UUID propertyTypeId);
    List<PropertyType> getAllPropertyType();
}
