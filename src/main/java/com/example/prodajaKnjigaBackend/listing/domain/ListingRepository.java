package com.example.prodajaKnjigaBackend.listing.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;

public interface ListingRepository extends JpaRepository<ListingEntity,Long>, JpaSpecificationExecutor<ListingEntity> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM listing_books WHERE listing_id = :listingId", nativeQuery = true)
    void deleteListingBooks(@Param("listingId") Long listingId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favorite WHERE listing_id = :listingId", nativeQuery = true)
    void deleteListingFavorites(@Param("listingId") Long listingId);
}
