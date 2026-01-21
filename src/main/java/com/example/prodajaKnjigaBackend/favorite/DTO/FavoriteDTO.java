package com.example.prodajaKnjigaBackend.favorite.DTO;

import com.example.prodajaKnjigaBackend.listing.DTO.ListingDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteDTO {
    private Long id;
    private ListingDTO listing;
    private LocalDateTime createdAt;
}