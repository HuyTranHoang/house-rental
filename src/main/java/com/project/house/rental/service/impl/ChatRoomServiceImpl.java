package com.project.house.rental.service.impl;

import com.project.house.rental.entity.ChatRoom;
import com.project.house.rental.repository.ChatRoomRepository;
import com.project.house.rental.service.ChatRoomService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public String getChatRoomId(long senderId, long receiverId, boolean createIfNotExist) {
        ChatRoom chatRoom = chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId);

        if (chatRoom == null && createIfNotExist) {
            return createChatId(senderId, receiverId);
        }

        if (chatRoom == null) {
            return null;
        }

        return chatRoom.getId();
    }

    private String createChatId(long senderId, long receiverId) {
        String chatId = senderId + "_" + receiverId;

        ChatRoom senderReceiver = ChatRoom.builder()
                .id(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        ChatRoom receiverSender = ChatRoom.builder()
                .id(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();

        chatRoomRepository.save(senderReceiver);
        chatRoomRepository.save(receiverSender);
        return chatId;
    }
}
