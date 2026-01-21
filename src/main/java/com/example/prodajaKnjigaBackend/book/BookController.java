package com.example.prodajaKnjigaBackend.book;

import com.example.prodajaKnjigaBackend.book.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<?> getAllBooks(){return ResponseEntity.ok(bookService.getAllBooks());}
}
