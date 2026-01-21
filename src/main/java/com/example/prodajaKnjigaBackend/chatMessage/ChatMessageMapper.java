package com.example.prodajaKnjigaBackend.chatMessage;

import com.example.prodajaKnjigaBackend.chatMessage.DTO.ChatMessageDTO;
import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageEntity;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    public static ChatMessageDTO toDto(ChatMessageEntity entity) {
        if (entity == null) return null;

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(entity.getId());
        dto.setChatRoomId(entity.getChatRoomEntity().getId());
        dto.setContent(entity.getContent());
        dto.setSentAt(entity.getSentAt());
        dto.setSender(UserMapper.toDto(entity.getSender()));

        return dto;
    }

}