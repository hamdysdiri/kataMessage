package com.messaging.bank.controller;

import com.messaging.bank.controller.constant.RestConstants;
import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.service.MessageMQService;
import com.messaging.bank.service.MessageStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MessageProcessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageMQService messageService;

    @MockitoBean
    private MessageStorageService messageStorageService;

    @Test
    @DisplayName("POST /api/v2/messages/"+RestConstants.CREATE+" should send a message")
    void testSendMessage() throws Exception {
        doNothing().when(messageService).sendAndSaveMessage(anyString());

        mockMvc.perform(post("/api/v2/messages/"+RestConstants.CREATE)
                        .content("Hello MQ")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully"));
    }

    @Test
    @DisplayName("GET /api/v2/messages/"+RestConstants.LAST_RECEIVE+" should return last message if exists")
    void testReceiveLastMessage() throws Exception {
        MessageEntity message = new MessageEntity("Last Message","123", LocalDateTime.now(), Direction.INBOUND);

        when(messageStorageService.getLastInboundMessage()).thenReturn(message);

        mockMvc.perform(get("/api/v2/messages/"+RestConstants.LAST_RECEIVE))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Received message")));
    }

    @Test
    @DisplayName("GET /api/v2/messages/"+RestConstants.GET_ALL+" should return all messages if any")
    void testReceiveAllMessages() throws Exception {
        MessageEntity message = new MessageEntity("Stored message","567", LocalDateTime.now(), Direction.INBOUND);
        when(messageStorageService.getAllMessagesStorage()).thenReturn(List.of(message));

        mockMvc.perform(get("/api/v2/messages/"+RestConstants.GET_ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("content\":\"Stored message")));
    }

    @Test
    @DisplayName("GET /api/v2/messages/"+RestConstants.GET_ALL+" should return empty message when DB is empty")
    void testReceiveAllMessagesEmpty() throws Exception {
        when(messageStorageService.getAllMessagesStorage()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v2/messages/"+ RestConstants.GET_ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No message available")));
    }
}
