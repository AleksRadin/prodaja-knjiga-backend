package com.example.prodajaKnjigaBackend.favorite.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByListingIdAndUserId(Long listingId, Long userId);
    List<FavoriteEntity> findByUserId(Long userId);
}
