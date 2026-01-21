package com.example.prodajaKnjigaBackend.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByTitleAndAuthorAndPublisher(String title, String author, String publisher);
}
