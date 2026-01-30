package com.example.prodajaKnjigaBackend.book;

import com.example.prodajaKnjigaBackend.author.AuthorMapper;
import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public static BookDTO toDto(BookEntity entity) {
        if (entity == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthors(entity.getAuthors().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toSet()));
        dto.setPublisher(entity.getPublisher());

        return dto;
    }

    public static BookEntity toEntity(BookDTO dto) {
        if (dto == null) return null;

        BookEntity entity = new BookEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setAuthors(dto.getAuthors().stream()
                .map(AuthorMapper::toEntity)
                .collect(Collectors.toSet()));
        entity.setPublisher(dto.getPublisher());

        return entity;
    }
}
