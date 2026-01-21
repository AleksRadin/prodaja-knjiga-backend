package com.example.prodajaKnjigaBackend.chatMessage.domain;

import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatRoomEntityOrderBySentAtAsc(ChatRoomEntity chatRoomEntity);
}
