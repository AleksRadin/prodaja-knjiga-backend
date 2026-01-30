package com.example.prodajaKnjigaBackend.book.DTO;

import com.example.prodajaKnjigaBackend.author.DTO.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdate {
    private Long id;
    private String title;
    private Set<AuthorDTO> authors;
    private String publisher;
}
