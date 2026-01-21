package com.example.prodajaKnjigaBackend.listing;

import com.example.prodajaKnjigaBackend.listing.DTO.ListingRequest;
import com.example.prodajaKnjigaBackend.listing.DTO.ListingUpdate;
import com.example.prodajaKnjigaBackend.listing.service.ListingService;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/listings")
@AllArgsConstructor
public class ListingController {
    private final ListingService listingService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createListing(@RequestBody ListingRequest request) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            listingService.createListing(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Success: Listing has been created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not create listing. Please check your data.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllListings(@RequestParam(value = "filter", required = false) String filter, @RequestParam(defaultValue = "false") Boolean fav, Pageable pageable) {
        Long currentUserId = null;
        if (Boolean.TRUE.equals(fav)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserEntity user) {
                currentUserId = user.getId();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return ResponseEntity.ok(listingService.getAllListings(filter, fav, currentUserId, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteListing(@PathVariable Long id){
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            listingService.deleteListing(id);
            return ResponseEntity.ok("Success: Listing has been deleted successfully!");
        } catch (Exception e) {
            //System.err.println("Delete error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not delete the listing. It might not exist or you don't have permission.");
        }
    }

    @PutMapping("/update/{listingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateListing(@PathVariable Long listingId, @RequestBody ListingUpdate request) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            listingService.updateListing(listingId, request);
            return ResponseEntity.ok("Success: Listing has been updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Failed to update listing.");
        }
    }



}
