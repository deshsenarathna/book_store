package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    // Add a new book
    public Book addBook(Book book) {
        return repo.save(book);
    }

    // Get all books
    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    // Update an existing book
    public Book updateBook(Long id, Book book) {
        Book existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setPrice(book.getPrice());
        existing.setIsbn(book.getIsbn());
        existing.setStock(book.getStock());

        return repo.save(existing);
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        repo.deleteById(id);
    }

    // Optional: Get a book by ID
    public Book getBookById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
}
