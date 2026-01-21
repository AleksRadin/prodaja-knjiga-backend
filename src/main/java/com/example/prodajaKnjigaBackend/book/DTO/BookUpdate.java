package com.example.prodajaKnjigaBackend.book.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdate {
    private Long id;
    private String title;
    private String author;
    private String publisher;
}
