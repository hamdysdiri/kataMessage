package com.messaging.bank.controller;

import com.messaging.bank.binder.PartnerDTO;
import com.messaging.bank.binder.PartnerRequestDTO;
import com.messaging.bank.controller.constant.RestConstants;
import com.messaging.bank.service.PartnerService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController{

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping(RestConstants.GET_ALL)
    public List<PartnerDTO> listAllPartners() {
        return partnerService.getAllPartners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable final Long id) {
        PartnerDTO dto = partnerService.getPartner(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(RestConstants.CREATE)
    public ResponseEntity<PartnerDTO> createPartner(@RequestBody @Valid final PartnerRequestDTO request) throws BadRequestException {
        PartnerDTO created = partnerService.createPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping(RestConstants.DELETE+"/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable final Long id) throws ChangeSetPersister.NotFoundException {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }

}
