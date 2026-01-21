package com.example.prodajaKnjigaBackend.book.service;

import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
}
