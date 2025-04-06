package com.messaging.bank.controller;

import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.service.MessageMQService;
import com.messaging.bank.service.MessageStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageMQService messageService;

    @MockBean
    private MessageStorageService messageStorageService;

    @Test
    @DisplayName("shouldSendMessageSuccessfully successfully to middleware ")
    void shouldSendMessageSuccessfully() throws Exception {
        mockMvc.perform(post("/api/messages/create")
                        .content("Hello Test")
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully"));
    }

    @Test
    @DisplayName("shouldReturnReceivedMessage successfully from middleware")
    void shouldReturnReceivedMessage() throws Exception {
        Mockito.when(messageService.receiveAndSaveMessage()).thenReturn("Hello Test");

        mockMvc.perform(get("/api/messages/receive"))
                .andExpect(status().isOk())
                .andExpect(content().string("Received message : Hello Test"));
    }

    @Test
    @DisplayName("shouldReturn all ReceivedMessage successfully from the db")
    void shouldReturnAllReceivedMessageFromDB() throws Exception {
        MessageEntity entity = new MessageEntity("JMS123","Hello MQ", LocalDateTime.now(), true);
        Mockito.when(messageStorageService.getAllMessagesStorage())
                .thenReturn(List.of(entity));

        mockMvc.perform(get("/api/messages/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Received message")));
    }
}
