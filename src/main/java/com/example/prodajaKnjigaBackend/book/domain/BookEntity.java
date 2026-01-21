package com.example.prodajaKnjigaBackend.book.domain;

import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 100)
    private String publisher;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<ListingEntity> listingEntities;

}
