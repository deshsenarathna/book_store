package com.bookstore.bookstore.stepdefinition;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repository.BookRepository;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddBookSteps {

    @Autowired
    private BookRepository repo;

    private Book book;

    @Given("the book store is empty")
    public void the_book_store_is_empty() {
        repo.deleteAll();
    }

    @When("I add a book with title {string}, author {string}, price {double}")
    public void i_add_a_book_with_title_author_price(String title, String author, Double price) {
        book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setStock(1);
        book = repo.save(book);
    }

    @Then("the book list should contain {int} book")
    public void the_book_list_should_contain_book(Integer count) {
        List<Book> books = repo.findAll();
        assertEquals(count, books.size());
    }

    @Then("the book title should be {string}")
    public void the_book_title_should_be(String expectedTitle) {
        assertEquals(expectedTitle, book.getTitle());
    }
}
