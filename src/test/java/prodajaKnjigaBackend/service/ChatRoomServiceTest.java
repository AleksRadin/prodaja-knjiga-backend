package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.chatRoom.DTO.ChatRoomDTO;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomEntity;
import com.example.prodajaKnjigaBackend.chatRoom.domain.ChatRoomRepository;
import com.example.prodajaKnjigaBackend.chatRoom.service.ChatRoomServiceImpl;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    private UserEntity currentUser;
    private UserEntity otherUser;

    @BeforeEach
    void setUp() {
        currentUser = new UserEntity();
        currentUser.setId(1L);
        currentUser.setEmail("me@example.com");

        otherUser = new UserEntity();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
    }

    @Test
    void getOrCreateChatRoom_shouldReturnExistingRoom() {
        ChatRoomEntity existingRoom = new ChatRoomEntity(100L, currentUser, otherUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(currentUser);
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRoomRepository.findByParticipants(currentUser, otherUser)).thenReturn(Optional.of(existingRoom));

        ChatRoomDTO result = chatRoomService.getOrCreateChatRoom(2L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(chatRoomRepository, never()).save(any());
    }

    @Test
    void getOrCreateChatRoom_shouldCreateNewRoom_whenNoneExists() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(currentUser);
        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(chatRoomRepository.findByParticipants(currentUser, otherUser)).thenReturn(Optional.empty());

        ChatRoomEntity newRoom = new ChatRoomEntity(200L, currentUser, otherUser);
        when(chatRoomRepository.save(any(ChatRoomEntity.class))).thenReturn(newRoom);

        ChatRoomDTO result = chatRoomService.getOrCreateChatRoom(2L);

        assertNotNull(result);
        assertEquals(200L, result.getId());
        verify(chatRoomRepository, times(1)).save(any(ChatRoomEntity.class));
    }

    @Test
    void getOrCreateChatRoom_shouldThrowException_whenChattingWithSelf() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(currentUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));

        assertThrows(IllegalArgumentException.class, () ->
                chatRoomService.getOrCreateChatRoom(1L)
        );
    }

    @Test
    void getOrCreateChatRoom_shouldThrowException_whenUserNotFound() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(currentUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                chatRoomService.getOrCreateChatRoom(99L)
        );
        assertEquals("Target user not found", exception.getMessage());
    }
}