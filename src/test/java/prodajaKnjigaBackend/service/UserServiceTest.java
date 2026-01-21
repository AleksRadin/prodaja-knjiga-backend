package prodajaKnjigaBackend.service;
import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.security.DTO.UserResource;
import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import com.example.prodajaKnjigaBackend.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserResourceDtoById_success() {
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFirstname("Petar");
        user.setLastname("Petrović");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserResource result = userService.findUserResourceDtoById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Petar", result.getFirstName());
        assertEquals("Petrović", result.getLastName());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserResourceDtoById_userNotFound() {
        Long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findUserResourceDtoById(userId)
        );

        assertEquals(
                "Korisnik s ID-jem " + userId + " nije pronađen.",
                exception.getMessage()
        );

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserDtoById_success() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFirstname("Marko");
        user.setLastname("Marković");
        user.setEmail("marko@example.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserDTO result = userService.findUserDtoById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Marko", result.getFirstname());
        assertEquals("Marković", result.getLastname());
        assertEquals("marko@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserDtoById_userNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findUserDtoById(userId)
        );

        assertEquals("Korisnik nije pronađen.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }
}
