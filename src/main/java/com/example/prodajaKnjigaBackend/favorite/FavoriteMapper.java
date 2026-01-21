package com.example.prodajaKnjigaBackend.favorite;

import com.example.prodajaKnjigaBackend.favorite.DTO.FavoriteDTO;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteEntity;
import com.example.prodajaKnjigaBackend.listing.ListingMapper;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public static FavoriteDTO toDto(FavoriteEntity entity) {
        if (entity == null) return null;

        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setListing(ListingMapper.toDto(entity.getListing()));

        return dto;
    }
}