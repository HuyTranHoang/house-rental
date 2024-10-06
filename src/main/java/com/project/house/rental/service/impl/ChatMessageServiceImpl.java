package com.project.house.rental.service.impl;

import com.project.house.rental.entity.ChatMessage;
import com.project.house.rental.repository.ChatMessageRepository;
import com.project.house.rental.service.ChatMessageService;
import com.project.house.rental.service.ChatRoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        String chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
        chatMessage.setChatId(chatId);

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String receiverId) {
        String chatId = chatRoomService.getChatRoomId(Long.parseLong(senderId), Long.parseLong(receiverId), false);
        return chatMessageRepository.findByChatId(chatId);
    }
}
