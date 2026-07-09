package com.library.model;

/**
 * Book – Domain model representing a library book.
 *
 * <p>A simple value object passed between the service and repository layers.
 */
public class Book {

    private final int    bookId;
    private final String title;
    private final String author;
    private final String isbn;

    public Book(int bookId, String title, String author, String isbn) {
        if (bookId <= 0)  throw new IllegalArgumentException("bookId must be positive.");
        if (title  == null || title.isBlank())  throw new IllegalArgumentException("title must not be blank.");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("author must not be blank.");
        this.bookId = bookId;
        this.title  = title;
        this.author = author;
        this.isbn   = (isbn != null) ? isbn.trim() : "N/A";
    }

    public int    getBookId() { return bookId; }
    public String getTitle()  { return title; }
    public String getAuthor() { return author; }
    public String getIsbn()   { return isbn; }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', isbn='%s'}",
                bookId, title, author, isbn);
    }
}
