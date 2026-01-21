package com.example.prodajaKnjigaBackend.listing.domain;

import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteEntity;
import com.example.prodajaKnjigaBackend.listing.BookCondition;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "listing")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ListingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "listing_books",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<BookEntity> books = new HashSet<>();

    @Column(nullable = false)
    private Double price;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition", length = 50, nullable = false)
    private BookCondition condition;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL)
    private Set<FavoriteEntity> favoriteEntities = new HashSet<>();

}
