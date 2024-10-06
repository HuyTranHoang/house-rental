package com.project.house.rental.repository;

import com.project.house.rental.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findBySenderIdAndReceiverId(long senderId, long receiverId);
}
