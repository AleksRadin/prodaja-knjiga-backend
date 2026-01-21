package com.example.prodajaKnjigaBackend.chatMessage;

import com.example.prodajaKnjigaBackend.chatMessage.DTO.ChatMessageRequest;
import com.example.prodajaKnjigaBackend.chatMessage.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/chatMessage")
@AllArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createChatMessage(@RequestBody ChatMessageRequest request) {
        try {

//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    chatMessageService.createChatMessage(
                            request.getChatRoomId(),
                            request.getContent()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not send message. " + e.getMessage());
        }
    }

    @GetMapping("/{chatRoomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllChatMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getAllChatMessages(chatRoomId));
    }

}
