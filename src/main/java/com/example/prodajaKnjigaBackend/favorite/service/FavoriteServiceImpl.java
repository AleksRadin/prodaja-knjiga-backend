package com.example.prodajaKnjigaBackend.favorite.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.favorite.DTO.FavoriteDTO;
import com.example.prodajaKnjigaBackend.favorite.FavoriteMapper;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteEntity;
import com.example.prodajaKnjigaBackend.favorite.domain.FavoriteRepository;
import com.example.prodajaKnjigaBackend.listing.domain.ListingEntity;
import com.example.prodajaKnjigaBackend.listing.domain.ListingRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteRepository favoriteRepository;
    private final ListingRepository listingRepository;
    private final SecurityUtils securityUtils;

    @Override
    public void createFavorite(Long listingId) {
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        ListingEntity listingEntity = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("ListingEntity not found with id: " + listingId));

        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setListing(listingEntity);
        favoriteEntity.setUser(currentUser);
        favoriteEntity.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(favoriteEntity);
    }

    @Override
    public void deleteFavorite(Long listingId) {
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        FavoriteEntity favoriteEntityToDelete = favoriteRepository
                .findByListingIdAndUserId(listingId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favoriteEntityToDelete);
    }

    @Override
    public List<FavoriteDTO> getAllFavorites(){
        UserEntity currentUser = securityUtils.getAuthenticatedUser();

        return favoriteRepository.findByUserId(currentUser.getId())
                .stream()
                .map(FavoriteMapper::toDto)
                .collect(Collectors.toList());
    }

}
