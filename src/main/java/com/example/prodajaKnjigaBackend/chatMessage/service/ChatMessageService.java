package com.example.prodajaKnjigaBackend.chatMessage.service;

import com.example.prodajaKnjigaBackend.chatMessage.DTO.ChatMessageDTO;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;

import java.util.List;

public interface ChatMessageService {
    ChatMessageDTO createChatMessage(Long chatRoomId, String content);
    List<ChatMessageDTO> getAllChatMessages(Long chatRoomId);
}
