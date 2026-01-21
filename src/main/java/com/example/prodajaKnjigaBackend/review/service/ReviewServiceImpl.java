package com.example.prodajaKnjigaBackend.review.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.review.DTO.ReviewDTO;
import com.example.prodajaKnjigaBackend.review.ReviewMapper;
import com.example.prodajaKnjigaBackend.review.domain.ReviewEntity;
import com.example.prodajaKnjigaBackend.review.domain.ReviewRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.UserRole;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public ReviewDTO createReview(Long reviewedId, String comment){
        UserEntity reviewer = securityUtils.getAuthenticatedUser();

        UserEntity reviewed = userRepository.findById(reviewedId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        reviewRepository.findByReviewer_IdAndReviewed_Id(reviewer.getId(), reviewedId)
                .ifPresent(r -> {
                    throw new RuntimeException("You have already reviewed this user. Please edit your existing review.");
                });

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewer(reviewer);
        reviewEntity.setReviewed(reviewed);
        reviewEntity.setComment(comment);
        reviewEntity.setCreatedAt(LocalDateTime.now());
        return ReviewMapper.toDto(reviewRepository.save(reviewEntity));
    }

    @Override
    public void deleteReview(Long reviewId) {
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        boolean isOwner = review.getReviewer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public ReviewDTO updateReview(Long reviewId, String newComment){

        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        ReviewEntity reviewEntity = reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!reviewEntity.getReviewer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the author can edit this review");
        }

        reviewEntity.setComment(newComment);
        reviewEntity.setCreatedAt(LocalDateTime.now());
        return ReviewMapper.toDto(reviewRepository.save(reviewEntity));
    }

    @Override
    public List<ReviewDTO> getReviewsByReviewedUser(Long reviewedUserId) {
        return reviewRepository.findByReviewed_Id(reviewedUserId).stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }

}
