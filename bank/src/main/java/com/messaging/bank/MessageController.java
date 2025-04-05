package com.messaging.bank;

import com.messaging.bank.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String messageText) {
        messageService.sendMessage(messageText);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping("/receive")
    public ResponseEntity<String> receiveMessage() {
        String msg = messageService.receiveMessage();
        if (msg != null) {
            return ResponseEntity.ok("Received message: " + msg);
        } else {
            return ResponseEntity.ok("No message available.");
        }
    }
}
