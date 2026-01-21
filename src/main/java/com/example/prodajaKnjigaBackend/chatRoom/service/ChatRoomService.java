package com.example.prodajaKnjigaBackend.chatRoom.service;

import com.example.prodajaKnjigaBackend.chatRoom.DTO.ChatRoomDTO;

import java.util.List;

public interface ChatRoomService {
    ChatRoomDTO getOrCreateChatRoom(Long user2Id);
    List<ChatRoomDTO> getAllChatRooms();
}
