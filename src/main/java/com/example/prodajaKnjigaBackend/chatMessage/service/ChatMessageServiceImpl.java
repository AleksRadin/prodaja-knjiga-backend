package com.example.prodajaKnjigaBackend.chatMessage.service;

import com.example.prodajaKnjigaBackend.chatMessage.ChatMessageMapper;
import com.example.prodajaKnjigaBackend.chatMessage.DTO.ChatMessageDTO;
import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageEntity;
import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageRepository;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageRepository chatMessageRepository;
    private final SecurityUtils securityUtils;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessageDTO createChatMessage(Long chatRoomId, String content) {
        UserEntity sender = securityUtils.getAuthenticatedUser();

        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setChatRoomEntity(chatRoom);
        chatMessageEntity.setContent(content);
        chatMessageEntity.setSender(sender);
        chatMessageEntity.setSentAt(LocalDateTime.now());
        return ChatMessageMapper.toDto(chatMessageRepository.save(chatMessageEntity));
    }

    @Override
    public List<ChatMessageDTO> getAllChatMessages(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        return chatMessageRepository.findByChatRoomEntityOrderBySentAtAsc(chatRoomEntity)
                .stream()
                .map(ChatMessageMapper::toDto)
                .collect(Collectors.toList());
    }


}
