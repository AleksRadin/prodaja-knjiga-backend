package com.example.prodajaKnjigaBackend.favorite.domain;

import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "favorite")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private ListingEntity listing;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
