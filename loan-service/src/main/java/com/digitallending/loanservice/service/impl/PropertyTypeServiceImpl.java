package com.digitallending.loanservice.service.impl;

import com.digitallending.loanservice.exception.PropertyTypeNotFoundException;
import com.digitallending.loanservice.model.entity.PropertyType;
import com.digitallending.loanservice.repository.PropertyTypeRepository;
import com.digitallending.loanservice.service.def.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PropertyTypeServiceImpl implements PropertyTypeService {
    @Autowired
    private PropertyTypeRepository propertyTypeRepository;
    @Override
    public PropertyType getPropertyTypeById(UUID propertyTypeId) {
        return propertyTypeRepository
                .findById(propertyTypeId)
                .orElseThrow(
                        ()-> new PropertyTypeNotFoundException("loan type with id :- " + propertyTypeId + " is not found!!")
                );
    }

    @Override
    public List<PropertyType> getAllPropertyType() {
        return propertyTypeRepository
                .findAll();
    }
}
