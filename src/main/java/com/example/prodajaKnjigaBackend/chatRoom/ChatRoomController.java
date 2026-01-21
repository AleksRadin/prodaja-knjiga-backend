package com.example.prodajaKnjigaBackend.chatRoom;

import com.example.prodajaKnjigaBackend.chatRoom.service.ChatRoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/chatRoom")
@AllArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getChatRoom(Long user2Id) {
        return ResponseEntity.ok(chatRoomService.getOrCreateChatRoom(user2Id));
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }
}
