package com.example.prodajaKnjigaBackend.chatMessage.DTO;

import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomId;
    private String content;
}
