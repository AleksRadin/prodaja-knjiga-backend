package com.example.prodajaKnjigaBackend.chatRoom;

import com.example.prodajaKnjigaBackend.chatRoom.DTO.ChatRoomDTO;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public static ChatRoomDTO toDto(ChatRoomEntity entity) {
        if (entity == null) return null;

        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setId(entity.getId());
        dto.setUser1(UserMapper.toDto(entity.getUser1()));
        dto.setUser2(UserMapper.toDto(entity.getUser2()));

        return dto;
    }

}