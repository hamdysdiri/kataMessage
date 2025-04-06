package com.messaging.bank.service;

import com.messaging.bank.binder.EntityMapper;
import com.messaging.bank.binder.PartnerDTO;
import com.messaging.bank.binder.PartnerRequestDTO;
import com.messaging.bank.controller.exception.NotFoundException;
import com.messaging.bank.entities.PartnerEntity;
import com.messaging.bank.repository.PartnerRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private EntityMapper mapper;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;

    }

    @Transactional(readOnly = true)
    public PartnerDTO getPartner(final Long id) {
        PartnerEntity entity = partnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partner introuvable pour l'id " + id));
        return mapper.toPartnerDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<PartnerDTO> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(mapper::toPartnerDTO)
                .toList();
    }

    public PartnerDTO createPartner(PartnerRequestDTO request) throws BadRequestException {
        if (partnerRepository.existsByAlias(request.alias())) {
            throw new BadRequestException("Alias already exists: " + request.alias());
        }
        PartnerEntity entity = mapper.fromCreatePartnerRequest(request);
        PartnerEntity saved = partnerRepository.save(entity);
        return mapper.toPartnerDTO(saved);
    }

    public void deletePartner(Long id) throws ChangeSetPersister.NotFoundException {
        PartnerEntity entity = partnerRepository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
        partnerRepository.delete(entity);
    }
}
