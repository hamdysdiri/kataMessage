package com.messaging.bank.binder;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;

public record PartnerDTO(Long id,
                         String alias,
                         String type,
                         Direction direction,
                         String application,
                         ProcessedFlowType processedFlowType,
                         String description) {}
