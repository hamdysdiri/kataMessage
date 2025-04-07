package com.messaging.bank.binder;

import com.messaging.bank.entities.PartnerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    PartnerDTO toPartnerDTO(PartnerEntity entity);

    @Mapping(target = "id", ignore = true)
    PartnerEntity fromCreatePartnerRequest(PartnerRequestDTO request);
}