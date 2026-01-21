package com.example.prodajaKnjigaBackend.listing.DTO;

import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.listing.BookCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {
    private Set<BookDTO> books;
    private Double price;
    private BookCondition condition;
    private String description;
}
