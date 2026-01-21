package com.example.prodajaKnjigaBackend.book;

import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static BookDTO toDto(BookEntity entity) {
        if (entity == null) return null;

        BookDTO dto = new BookDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublisher(entity.getPublisher());

        return dto;
    }

    public static BookEntity toEntity(BookDTO dto) {
        if (dto == null) return null;

        BookEntity entity = new BookEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublisher(dto.getPublisher());

        return entity;
    }
}
