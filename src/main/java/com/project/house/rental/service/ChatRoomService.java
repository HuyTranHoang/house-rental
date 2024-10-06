package com.project.house.rental.service;

public interface ChatRoomService {
    String getChatRoomId(long senderId, long receiverId, boolean createIfNotExist);
}
