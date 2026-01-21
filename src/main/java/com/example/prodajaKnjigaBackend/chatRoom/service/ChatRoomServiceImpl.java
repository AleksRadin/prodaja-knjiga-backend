package com.example.prodajaKnjigaBackend.chatRoom.service;

import com.example.prodajaKnjigaBackend.chatRoom.ChatRoomMapper;
import com.example.prodajaKnjigaBackend.chatRoom.DTO.ChatRoomDTO;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public ChatRoomDTO getOrCreateChatRoom(Long user2Id) {
        UserEntity user1 = securityUtils.getAuthenticatedUser();

        UserEntity user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        if (user1.getId().equals(user2.getId())) {
            throw new IllegalArgumentException("You cannot start a chat with yourself.");
        }

        ChatRoomEntity chatRoom = chatRoomRepository.findByParticipants(user1, user2)
                .orElseGet(() -> {
                    ChatRoomEntity newRoom = new ChatRoomEntity();
                    newRoom.setUser1(user1);
                    newRoom.setUser2(user2);
                    return chatRoomRepository.save(newRoom);
                });
        return ChatRoomMapper.toDto(chatRoom);
    }

    @Override
    public List<ChatRoomDTO> getAllChatRooms(){
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        return chatRoomRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(ChatRoomMapper::toDto)
                .collect(Collectors.toList());
    }


}
