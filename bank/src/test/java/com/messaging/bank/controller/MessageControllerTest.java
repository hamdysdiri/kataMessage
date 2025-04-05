package com.messaging.bank.controller;

import com.messaging.bank.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    @DisplayName("shouldReturnReceivedMessage successfully ")
    void shouldSendMessageSuccessfully() throws Exception {
        mockMvc.perform(post("/api/messages/send")
                        .content("Hello Test")
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message sent successfully"));
    }

    @Test
    @DisplayName("shouldReturnReceivedMessage successfully")
    void shouldReturnReceivedMessage() throws Exception {
        Mockito.when(messageService.receiveAndSaveMessage()).thenReturn("Hello Test");

        mockMvc.perform(get("/api/messages/receive"))
                .andExpect(status().isOk())
                .andExpect(content().string("Received message : Hello Test"));
    }

}
