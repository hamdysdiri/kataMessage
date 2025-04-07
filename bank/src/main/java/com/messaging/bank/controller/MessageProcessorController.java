package com.messaging.bank.controller;

import com.messaging.bank.controller.constant.RestConstants;
import com.messaging.bank.entities.MessageEntity;
import com.messaging.bank.service.MessageMQService;
import com.messaging.bank.service.MessageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * this version 2 of controller is an advanced use of mq IBM. serve multiple push/pull messages.
 *
 */

@RestController
@RequestMapping("/api/v2/messages")
public class MessageProcessorController {

    private final MessageMQService messageService;
    private final MessageStorageService messageStorageService;

    public MessageProcessorController(MessageMQService messageService, MessageStorageService messageStorageService) {
        this.messageService = messageService;
        this.messageStorageService = messageStorageService;
    }

    @PostMapping(RestConstants.CREATE)
    public ResponseEntity<String> sendMessageToMiddelware(@RequestBody String messageText) {
        messageService.sendAndSaveMessage(messageText);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping(RestConstants.LAST_RECEIVE)
    public ResponseEntity<String> receiveLatestMessageFromMiddelware()  {
        MessageEntity msg = messageStorageService.getLastInboundMessage();
        if (msg != null) {
            return ResponseEntity.ok("Received message : " + msg);
        } else {
            return ResponseEntity.ok("No message available.");
        }
    }

    @GetMapping(RestConstants.GET_ALL)
    public ResponseEntity<String> receiveAllMessageFromDB() {
        List<MessageEntity> msg = messageStorageService.getAllMessagesStorage();
        if (msg.size() != 0) {
            return ResponseEntity.ok("Received message : " + msg);
        } else {
            return ResponseEntity.ok("No message available.");
        }
    }
}
