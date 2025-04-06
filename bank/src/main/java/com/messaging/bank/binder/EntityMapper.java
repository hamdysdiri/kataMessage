package com.messaging.bank.binder;

import com.messaging.bank.entities.PartnerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {


    PartnerDTO toPartnerDTO(PartnerEntity entity);

    PartnerEntity fromCreatePartnerRequest(PartnerRequestDTO request);
}

