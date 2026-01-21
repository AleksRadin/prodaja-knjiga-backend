package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.review.DTO.ReviewDTO;
import com.example.prodajaKnjigaBackend.review.domain.ReviewEntity;
import com.example.prodajaKnjigaBackend.review.domain.ReviewRepository;
import com.example.prodajaKnjigaBackend.review.service.ReviewServiceImpl;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.UserRole;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private UserRepository userRepository;
    @Mock private SecurityUtils securityUtils;

    @InjectMocks private ReviewServiceImpl reviewService;

    private UserEntity mockReviewer;
    private UserEntity mockReviewed;

    @BeforeEach
    void setUp() {
        mockReviewer = new UserEntity();
        mockReviewer.setId(1L);
        mockReviewer.setEmail("reviewer@test.com");
        mockReviewer.setRole(UserRole.REGULAR);

        mockReviewed = new UserEntity();
        mockReviewed.setId(2L);
    }

    @Test
    void createReview_success() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockReviewer);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockReviewed));
        when(reviewRepository.findByReviewer_IdAndReviewed_Id(1L, 2L)).thenReturn(Optional.empty());
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReviewDTO result = reviewService.createReview(2L, "Odlična saradnja");

        assertNotNull(result);
        assertEquals("Odlična saradnja", result.getComment());
        verify(reviewRepository).save(any(ReviewEntity.class));
    }

    @Test
    void createReview_alreadyReviewed_throwsException() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockReviewer);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockReviewed));
        when(reviewRepository.findByReviewer_IdAndReviewed_Id(1L, 2L)).thenReturn(Optional.of(new ReviewEntity()));

        assertThrows(RuntimeException.class, () -> reviewService.createReview(2L, "Novi komentar"));
    }

    @Test
    void deleteReview_asOwner_success() {
        Long reviewId = 100L;
        ReviewEntity review = new ReviewEntity();
        review.setReviewer(mockReviewer);

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockReviewer);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.deleteReview(reviewId);

        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_asAdmin_success() {
        UserEntity admin = new UserEntity();
        admin.setId(5L);
        admin.setRole(UserRole.ADMIN);

        ReviewEntity review = new ReviewEntity();
        review.setReviewer(mockReviewer);

        when(securityUtils.getAuthenticatedUser()).thenReturn(admin);
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(100L);

        verify(reviewRepository).delete(review);
    }

    @Test
    void updateReview_success() {
        Long reviewId = 100L;
        ReviewEntity existingReview = new ReviewEntity();
        existingReview.setId(reviewId);
        existingReview.setReviewer(mockReviewer);
        existingReview.setComment("Staro");

        when(securityUtils.getAuthenticatedUser()).thenReturn(mockReviewer);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ReviewDTO result = reviewService.updateReview(reviewId, "Novo");

        assertEquals("Novo", result.getComment());
        verify(reviewRepository).save(existingReview);
    }

    @Test
    void updateReview_unauthorized_throwsException() {
        UserEntity intruder = new UserEntity();
        intruder.setId(99L);

        ReviewEntity existingReview = new ReviewEntity();
        existingReview.setReviewer(mockReviewer); // Autor je ID 1

        when(securityUtils.getAuthenticatedUser()).thenReturn(intruder);
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(existingReview));

        assertThrows(AccessDeniedException.class, () -> reviewService.updateReview(100L, "Haker"));
    }

    @Test
    void getReviewsByReviewedUser_returnsList() {
        when(reviewRepository.findByReviewed_Id(2L)).thenReturn(List.of(new ReviewEntity()));

        List<ReviewDTO> result = reviewService.getReviewsByReviewedUser(2L);

        assertFalse(result.isEmpty());
        verify(reviewRepository).findByReviewed_Id(2L);
    }
}