package com.example.prodajaKnjigaBackend.author;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.author.DTO.AuthorDTO;

public class AuthorMapper {

    public static AuthorDTO toDto(AuthorEntity entity) {
        if (entity == null) return null;
        AuthorDTO dto = new AuthorDTO();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        return dto;
    }

    public static AuthorEntity toEntity(AuthorDTO dto) {
        if (dto == null) return null;
        AuthorEntity entity = new AuthorEntity();
        entity.setId(dto.getId());
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        return entity;
    }
}