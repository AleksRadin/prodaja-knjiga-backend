package com.example.prodajaKnjigaBackend.chatMessage.DTO;

import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long id;
    private Long chatRoomId;
    private UserDTO sender;
    private String content;
    private LocalDateTime sentAt;
}