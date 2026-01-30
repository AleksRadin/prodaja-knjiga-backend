package com.example.prodajaKnjigaBackend.author.domain;

import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 100)
    private String firstname;

    @EqualsAndHashCode.Include
    @Column(nullable = false, length = 100)
    private String lastname;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<BookEntity> books = new HashSet<>();
}