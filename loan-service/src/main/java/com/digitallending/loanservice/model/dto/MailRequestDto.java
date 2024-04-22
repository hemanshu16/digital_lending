package com.digitallending.loanservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MailRequestDto {
    private UUID userId;
    private String subject;
    private String message;
}
