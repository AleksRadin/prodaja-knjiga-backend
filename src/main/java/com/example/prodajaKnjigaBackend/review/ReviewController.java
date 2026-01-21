package com.example.prodajaKnjigaBackend.review;


import com.example.prodajaKnjigaBackend.review.DTO.ReviewDTO;
import com.example.prodajaKnjigaBackend.review.DTO.ReviewRequest;
import com.example.prodajaKnjigaBackend.review.DTO.ReviewUpdate;
import com.example.prodajaKnjigaBackend.review.domain.ReviewEntity;
import com.example.prodajaKnjigaBackend.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/review")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            ReviewDTO review = reviewService.createReview(request.getReviewedId(), request.getComment());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Success: Review created successfully!",
                    "data", review
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Could not create review.");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteReview(@PathVariable Long id){
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Success: Review deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Failed to delete review.");
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewUpdate request) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            String newComment = request.getComment();
            ReviewDTO updated = reviewService.updateReview(id, newComment);
            return ResponseEntity.ok(Map.of(
                    "message", "Success: Review updated successfully!",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Failed to update review.");
        }
    }

    @GetMapping("/user/{reviewedUserId}")
    public ResponseEntity<List<?>> getReviewsByReviewedUser(@PathVariable Long reviewedUserId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewedUser(reviewedUserId));
    }


}
