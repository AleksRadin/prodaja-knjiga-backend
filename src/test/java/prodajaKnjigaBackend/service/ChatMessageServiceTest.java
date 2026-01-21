package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.chatMessage.DTO.ChatMessageDTO;
import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageEntity;
import com.example.prodajaKnjigaBackend.chatMessage.domain.ChatMessageRepository;
import com.example.prodajaKnjigaBackend.chatMessage.service.ChatMessageServiceImpl;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    private UserEntity mockUser;
    private ChatRoomEntity mockChatRoom;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockChatRoom = new ChatRoomEntity();
        mockChatRoom.setId(1L);
    }

    @Test
    void createChatMessage_success() {
        String content = "Zdravo svima!";
        ChatMessageEntity savedEntity = new ChatMessageEntity();
        savedEntity.setId(10L);
        savedEntity.setContent(content);
        savedEntity.setSender(mockUser);
        savedEntity.setChatRoomEntity(mockChatRoom);
        savedEntity.setSentAt(LocalDateTime.now());

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(mockChatRoom));
        when(chatMessageRepository.save(any(ChatMessageEntity.class))).thenReturn(savedEntity);

        ChatMessageDTO result = chatMessageService.createChatMessage(1L, content);

        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(chatMessageRepository, times(1)).save(any(ChatMessageEntity.class));
    }

    @Test
    void createChatMessage_chatRoomNotFound_throwsException() {
        // Arrange
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                chatMessageService.createChatMessage(99L, "Hello"));

        assertEquals("ChatRoom not found", exception.getMessage());
    }

    @Test
    void getAllChatMessages_success() {
        ChatMessageEntity msg1 = new ChatMessageEntity(1L, mockChatRoom, mockUser, "Poruka 1", LocalDateTime.now());
        ChatMessageEntity msg2 = new ChatMessageEntity(2L, mockChatRoom, mockUser, "Poruka 2", LocalDateTime.now());

        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(mockChatRoom));
        when(chatMessageRepository.findByChatRoomEntityOrderBySentAtAsc(mockChatRoom))
                .thenReturn(List.of(msg1, msg2));

        List<ChatMessageDTO> result = chatMessageService.getAllChatMessages(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Poruka 1", result.get(0).getContent());
        verify(chatMessageRepository).findByChatRoomEntityOrderBySentAtAsc(any());
    }

    @Test
    void getAllChatMessages_chatRoomNotFound_throwsException() {
        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chatMessageService.getAllChatMessages(1L));
    }
}