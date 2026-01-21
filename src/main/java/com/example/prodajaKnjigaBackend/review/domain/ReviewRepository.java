package com.example.prodajaKnjigaBackend.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findByReviewer_IdAndReviewed_Id(Long reviewerId, Long reviewedId);
    Optional<ReviewEntity> findById(Long id);
    List<ReviewEntity> findByReviewed_Id(Long reviewedUserId);
}
