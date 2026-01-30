package com.example.prodajaKnjigaBackend.author.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findByFirstnameAndLastname(String firstname, String lastname);
}
