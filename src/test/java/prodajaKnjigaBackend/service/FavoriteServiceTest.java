package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.favorite.DTO.FavoriteDTO;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteEntity;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteRepository;
import com.example.prodajaKnjigaBackend.favorite.service.FavoriteServiceImpl;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingRepository;
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
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
    }

    @Test
    void createFavorite_success() {
        Long listingId = 10L;
        ListingEntity listing = new ListingEntity();
        listing.setId(listingId);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        favoriteService.createFavorite(listingId);
        verify(favoriteRepository, times(1)).save(any(FavoriteEntity.class));
    }

    @Test
    void createFavorite_listingNotFound_throwsException() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(listingRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                favoriteService.createFavorite(99L));
    }

    @Test
    void deleteFavorite_success() {
        Long listingId = 10L;
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setId(100L);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(favoriteRepository.findByListingIdAndUserId(listingId, mockUser.getId()))
                .thenReturn(Optional.of(favorite));

        favoriteService.deleteFavorite(listingId);

        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    void deleteFavorite_notFound_throwsException() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(favoriteRepository.findByListingIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                favoriteService.deleteFavorite(10L));
    }

    @Test
    void getAllFavorites_success() {
        FavoriteEntity f1 = new FavoriteEntity(1L, mockUser, new ListingEntity(), LocalDateTime.now());

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(favoriteRepository.findByUserId(mockUser.getId())).thenReturn(List.of(f1));

        List<FavoriteDTO> result = favoriteService.getAllFavorites();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(favoriteRepository).findByUserId(mockUser.getId());
    }
}