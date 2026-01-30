package com.example.prodajaKnjigaBackend.book.DTO;

import com.example.prodajaKnjigaBackend.author.DTO.AuthorDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private Set<AuthorDTO> authors;
    private String publisher;

}
