package com.example.prodajaKnjigaBackend.review.service;

import com.example.prodajaKnjigaBackend.review.DTO.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(Long reviewedId, String comment);
    void deleteReview(Long reviewed);
    ReviewDTO updateReview(Long reviewId, String newComment);
    List<ReviewDTO> getReviewsByReviewedUser(Long reviewedUserId);
}
