package com.example.prodajaKnjigaBackend.review;

import com.example.prodajaKnjigaBackend.review.DTO.ReviewDTO;
import com.example.prodajaKnjigaBackend.review.domain.ReviewEntity;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public static ReviewDTO toDto(ReviewEntity entity) {
        if (entity == null) return null;
        return ReviewDTO.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .reviewer(UserMapper.toDto(entity.getReviewer()))
                .reviewed(UserMapper.toDto(entity.getReviewed()))
                .build();
    }
}