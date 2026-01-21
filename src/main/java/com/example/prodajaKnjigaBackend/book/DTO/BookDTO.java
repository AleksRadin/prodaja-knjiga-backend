package com.example.prodajaKnjigaBackend.book.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String publisher;

}
