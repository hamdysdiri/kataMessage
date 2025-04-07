package com.messaging.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.bank.binder.PartnerDTO;
import com.messaging.bank.binder.PartnerRequestDTO;
import com.messaging.bank.controller.constant.RestConstants;
import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;
import com.messaging.bank.service.PartnerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v2/partners"+RestConstants.CREATE+ "should return list of partners")
    void testGetAllPartners() throws Exception {
        PartnerDTO dto = new PartnerDTO(1L, "TestAlias", "TYPE", Direction.INBOUND, "App", ProcessedFlowType.MESSAGE, "desc");
        Mockito.when(partnerService.getAllPartners()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v2/partners/"+ RestConstants.GET_ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alias").value("TestAlias"));
    }

    @Test
    @DisplayName("POST /api/v1/partners/"+RestConstants.CREATE+" should create a new partner")
    void testCreatePartner() throws Exception {
        PartnerRequestDTO request = new PartnerRequestDTO("Alias", "TYPE", Direction.OUTBOUND, "App", ProcessedFlowType.ALERTING, "description");
        PartnerDTO response = new PartnerDTO(1L, "Alias", "TYPE", Direction.OUTBOUND, "App", ProcessedFlowType.ALERTING, "description");

        Mockito.when(partnerService.createPartner(any())).thenReturn(response);

        mockMvc.perform(post("/api/v2/partners/"+RestConstants.CREATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").value("Alias"));
    }

    @Test
    @DisplayName("GET /api/v2/partners/{id} should return partner by ID")
    void testGetPartnerById() throws Exception {
        PartnerDTO dto = new PartnerDTO(1L, "TestAlias", "TYPE", Direction.INBOUND, "App", ProcessedFlowType.NOTIFICATION, "desc");
        Mockito.when(partnerService.getPartner(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v2/partners/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value("TestAlias"));
    }

    @Test
    @DisplayName("DELETE /api/v2/partners/{id} should delete partner")
    void testDeletePartner() throws Exception {
        Mockito.doNothing().when(partnerService).deletePartner(1L);

        mockMvc.perform(delete("/api/v2/partners/delete/1"))
                .andExpect(status().isNoContent());
    }
}
