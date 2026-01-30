package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.author.DTO.AuthorDTO;
import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.author.domain.AuthorRepository;
import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.book.domain.BookRepository;
import com.example.prodajaKnjigaBackend.listing.BookCondition;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingDTO;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingRequest;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingUpdate;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingRepository;
import com.example.prodajaKnjigaBackend.listing.service.ListingServiceImpl;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock private ListingRepository listingRepository;
    @Mock private BookRepository bookRepository;
    @Mock private SecurityUtils securityUtils;
    @Mock private AuthorRepository authorRepository;

    @InjectMocks private ListingServiceImpl listingService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("vlasnik@test.com");
    }

    @Test
    void createListing_success() {
        AuthorDTO authorDto = new AuthorDTO(null, "Ivo", "Andrić");
        BookDTO bookDto = new BookDTO(null, "Na Drini ćuprija", Set.of(authorDto), "Delfi");

        ListingRequest request = new ListingRequest();
        request.setBooks(Set.of(bookDto));
        request.setPrice(1500.0);
        request.setCondition(BookCondition.NEW);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);

        when(authorRepository.findByFirstnameAndLastname("Ivo", "Andrić")).thenReturn(Optional.empty());
        when(authorRepository.save(any(AuthorEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        when(bookRepository.findByTitleAndPublisher(any(), any())).thenReturn(Optional.empty());
        when(bookRepository.save(any(BookEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        when(listingRepository.save(any(ListingEntity.class))).thenAnswer(i -> {
            ListingEntity le = (ListingEntity) i.getArguments()[0];
            le.setId(10L);
            return le;
        });

        ListingDTO result = listingService.createListing(request);

        assertNotNull(result);
        verify(authorRepository).save(any(AuthorEntity.class));
        verify(bookRepository).save(any(BookEntity.class));
        verify(listingRepository).save(any(ListingEntity.class));
    }

    @Test
    void getAllListings_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        ListingEntity listing = new ListingEntity();
        Page<ListingEntity> page = new PageImpl<>(List.of(listing));

        when(listingRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable)))
                .thenReturn(page);

        Page<ListingDTO> result = listingService.getAllListings("filter", false, 1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(listingRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable));
    }

    @Test
    void deleteListing_asOwner_success() {
        Long listingId = 100L;
        ListingEntity listing = new ListingEntity();
        listing.setUser(mockUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(securityUtils.hasRole("ADMIN")).thenReturn(false);
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        listingService.deleteListing(listingId);

        verify(listingRepository).deleteListingBooks(listingId);
        verify(listingRepository).deleteListingFavorites(listingId);
        verify(listingRepository).deleteById(listingId);
    }

    @Test
    void deleteListing_asAdmin_success() {
        Long listingId = 100L;
        UserEntity admin = new UserEntity();
        admin.setEmail("admin@test.com");

        ListingEntity listing = new ListingEntity();
        listing.setUser(mockUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(admin);
        when(securityUtils.hasRole("ADMIN")).thenReturn(true);
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        listingService.deleteListing(listingId);

        verify(listingRepository).deleteById(listingId);
    }

    @Test
    void deleteListing_unauthorized_throwsAccessDenied() {
        UserEntity intruder = new UserEntity();
        intruder.setEmail("intruder@test.com");
        ListingEntity listing = new ListingEntity();
        listing.setUser(mockUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(intruder);
        when(securityUtils.hasRole("ADMIN")).thenReturn(false);
        when(listingRepository.findById(anyLong())).thenReturn(Optional.of(listing));

        assertThrows(AccessDeniedException.class, () -> listingService.deleteListing(100L));
    }

    @Test
    void updateListing_success() {
        Long listingId = 100L;
        ListingUpdate update = new ListingUpdate();
        update.setBooks(Collections.emptySet());
        update.setPrice(2000.0);

        ListingEntity existingListing = new ListingEntity();
        existingListing.setUser(mockUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(existingListing));
        when(listingRepository.save(any(ListingEntity.class))).thenReturn(existingListing);

        ListingDTO result = listingService.updateListing(listingId, update);

        assertNotNull(result);
        assertEquals(2000.0, result.getPrice());
        verify(listingRepository).save(any(ListingEntity.class));
    }

    @Test
    void updateListing_notOwner_throwsAccessDenied() {
        Long listingId = 100L;
        UserEntity intruder = new UserEntity();
        intruder.setId(99L);

        ListingEntity existingListing = new ListingEntity();
        existingListing.setUser(mockUser);

        when(securityUtils.getAuthenticatedUser()).thenReturn(intruder);
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(existingListing));

        assertThrows(AccessDeniedException.class, () ->
                listingService.updateListing(listingId, new ListingUpdate()));
    }
}