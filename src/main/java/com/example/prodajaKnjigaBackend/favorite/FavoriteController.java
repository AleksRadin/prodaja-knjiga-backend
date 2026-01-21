package com.example.prodajaKnjigaBackend.favorite;

import com.example.prodajaKnjigaBackend.favorite.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/favorite")
@AllArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createFavorite(@PathVariable Long id){
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            favoriteService.createFavorite(id);
            return ResponseEntity.ok("Added to favorites!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not add to favorites.");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long id){
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }
            favoriteService.deleteFavorite(id);
            return ResponseEntity.ok("Removed from favorites!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not remove from favorites.");
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

}


