package com.project.house.rental.service;


import com.project.house.rental.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findChatMessages(String senderId, String receiverId);
}
