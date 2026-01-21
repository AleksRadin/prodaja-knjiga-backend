package com.example.prodajaKnjigaBackend.listing.DTO;

import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.listing.BookCondition;
import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDTO {
    private Long id;
    private UserDTO user;
    private Set<BookDTO> books;
    private Double price;
    private LocalDate createdAt;
    private BookCondition condition;
    private String description;
}
