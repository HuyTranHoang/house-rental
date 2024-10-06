package com.project.house.rental.controller;

import com.project.house.rental.entity.ChatMessage;
import com.project.house.rental.entity.ChatNotification;
import com.project.house.rental.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat")
    public void processMessage(ChatMessage chatMessage) {
        ChatMessage saveMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiverId()), "/queue/messages",
                ChatNotification.builder()
                        .id(saveMsg.getId())
                        .senderId(saveMsg.getSenderId())
                        .receiverId(saveMsg.getReceiverId())
                        .message(saveMsg.getMessage())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId, @PathVariable String receiverId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
    }
}
