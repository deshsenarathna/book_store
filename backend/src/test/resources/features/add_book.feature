Feature: Add a new book
  As a user
  I want to add a new book to the store
  So that it appears in the book list

  Scenario: Add a book successfully
    Given the book store is empty
    When I add a book with title "The Hobbit", author "J.R.R. Tolkien", price 15.5
    Then the book list should contain 1 book
    And the book title should be "The Hobbit"
