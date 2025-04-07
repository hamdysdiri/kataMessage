package com.messaging.bank.binder;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PartnerRequestDTO(
        @NotBlank
        String alias,
        @NotBlank
        String type,
        @NotNull
        Direction direction,
        String application,
        @NotNull
        ProcessedFlowType processedFlowType,
        @NotBlank
        String description) {}
