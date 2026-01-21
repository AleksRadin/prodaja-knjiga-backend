package com.example.prodajaKnjigaBackend.book.service;

import com.example.prodajaKnjigaBackend.book.BookMapper;
import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.book.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }
}
