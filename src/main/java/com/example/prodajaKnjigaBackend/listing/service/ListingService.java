package com.example.prodajaKnjigaBackend.listing.service;

import com.example.prodajaKnjigaBackend.listing.DTO.ListingDTO;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingRequest;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ListingService {
    ListingDTO createListing(ListingRequest request);
    Page<ListingDTO> getAllListings(String filter, Boolean fav, Long userId, Pageable pageable);
    void deleteListing(Long listingId);
    ListingDTO updateListing(Long listingId, ListingUpdate listingUpdate);
}
