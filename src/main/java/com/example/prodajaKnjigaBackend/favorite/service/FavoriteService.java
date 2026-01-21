package com.example.prodajaKnjigaBackend.favorite.service;

import com.example.prodajaKnjigaBackend.favorite.DTO.FavoriteDTO;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;

import java.util.List;

public interface FavoriteService {
    void createFavorite(Long listingId);
    void deleteFavorite(Long listingId);
    List<FavoriteDTO> getAllFavorites();
}
