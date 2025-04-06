package com.messaging.bank.binder;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;
import jakarta.validation.constraints.NotNull;

public record PartnerRequestDTO(
        @NotNull
        String alias,
        @NotNull
        String type,
        @NotNull
        Direction direction,
        String application,
        @NotNull
        ProcessedFlowType processedFlowType,
        @NotNull
        String description) {}
