package com.example.prodajaKnjigaBackend.book.domain;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<AuthorEntity> authors = new HashSet<>();

    @Column(nullable = false, length = 100)
    private String publisher;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<ListingEntity> listingEntities;

}
