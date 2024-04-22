package com.digitallending.breservice.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BREConfigRequestDTO<T> {
    @NotEmpty(message = "List must not be Empty")
    private List<@Valid T> data;
    @NotNull(message = "Id must not be Empty")
    private UUID id;
}
