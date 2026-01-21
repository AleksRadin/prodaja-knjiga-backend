package com.example.prodajaKnjigaBackend.chatRoom.DTO;

import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDTO {
    private Long id;
    private UserDTO user1;
    private UserDTO user2;
}