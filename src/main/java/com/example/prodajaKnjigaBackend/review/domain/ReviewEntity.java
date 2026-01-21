package com.example.prodajaKnjigaBackend.review.domain;

import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Korisnik koji piše recenziju
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserEntity reviewer;

    // Korisnik koji je ocenjen
    @ManyToOne
    @JoinColumn(name = "reviewed_id", nullable = false)
    private UserEntity reviewed;

    @Column(length = 255)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
