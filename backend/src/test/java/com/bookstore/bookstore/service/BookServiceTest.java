package com.bookstore.bookstore.service;
import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Test
    void shouldAddBookSuccessfully() {
        BookRepository repo = Mockito.mock(BookRepository.class);
        BookService service = new BookService(repo);

        Book book = new Book(null,"Title","Author",10.0,"",1);
        when(repo.save(book)).thenReturn(new Book(1L,"Title","Author",10.0,"",1));

        Book saved = service.addBook(book);

        assertNotNull(saved.getId());
        assertEquals("Title", saved.getTitle());
    }

}
