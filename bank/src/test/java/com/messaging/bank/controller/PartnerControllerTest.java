package com.messaging.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.bank.binder.PartnerDTO;
import com.messaging.bank.binder.PartnerRequestDTO;
import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;
import com.messaging.bank.service.PartnerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartnerController.class)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllPartners() throws Exception {
        PartnerDTO partner = new PartnerDTO(1L, "test-alias", "type-A", Direction.INBOUND, "App", ProcessedFlowType.MESSAGE, "desc");
        Mockito.when(partnerService.getAllPartners()).thenReturn(List.of(partner));

        mockMvc.perform(get("/api/partners/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].alias", is("test-alias")));
    }

    @Test
    void shouldCreatePartner() throws Exception {
       PartnerRequestDTO request = new PartnerRequestDTO("alias", "type", Direction.OUTBOUND, "AppX", ProcessedFlowType.ALERTING, "desc");
        PartnerDTO response = new PartnerDTO(1L, "alias", "type", Direction.OUTBOUND, "AppX", ProcessedFlowType.ALERTING, "desc");

        Mockito.when(partnerService.createPartner(any())).thenReturn(response);

        mockMvc.perform(post("/api/partners/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias", is("alias")));
    }

    @Test
    void shouldDeletePartner() throws Exception {
        mockMvc.perform(delete("/api/partners/delete/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(partnerService).deletePartner(1L);
    }
}
