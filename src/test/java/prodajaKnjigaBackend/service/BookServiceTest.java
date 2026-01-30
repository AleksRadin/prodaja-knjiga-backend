package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.author.domain.AuthorEntity;
import com.example.prodajaKnjigaBackend.book.DTO.BookDTO;
import com.example.prodajaKnjigaBackend.book.domain.BookEntity;
import com.example.prodajaKnjigaBackend.book.domain.BookRepository;
import com.example.prodajaKnjigaBackend.book.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_returnsList() {
        AuthorEntity author = new AuthorEntity();
        author.setId(1L);
        author.setFirstname("Ivo");
        author.setLastname("Andrić");

        BookEntity book1 = new BookEntity();
        book1.setId(1L);
        book1.setTitle("Na Drini ćuprija");
        book1.setAuthors(Set.of(author));
        book1.setPublisher("Delfi");

        BookEntity book2 = new BookEntity();
        book2.setId(2L);
        book2.setTitle("Prokleta avlija");
        book2.setAuthors(Set.of(author));
        book2.setPublisher("Laguna");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Na Drini ćuprija", result.get(0).getTitle());
        assertEquals("Prokleta avlija", result.get(1).getTitle());

        assertFalse(result.get(0).getAuthors().isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllBooks_returnsEmptyList() {
        when(bookRepository.findAll()).thenReturn(List.of());

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }
}
