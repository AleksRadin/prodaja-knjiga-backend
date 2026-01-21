package com.example.prodajaKnjigaBackend.listing;

import com.example.prodajaKnjigaBackend.book.BookMapper;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingDTO;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ListingMapper {

    public static ListingDTO toDto(ListingEntity entity) {
        if (entity == null) return null;

        ListingDTO dto = new ListingDTO();
        dto.setId(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCondition(entity.getCondition());
        dto.setDescription(entity.getDescription());

        dto.setUser(UserMapper.toDto(entity.getUser()));
        dto.setBooks(
                entity.getBooks()
                        .stream()
                        .map(BookMapper::toDto)
                        .collect(Collectors.toSet())
        );

        return dto;
    }
}
