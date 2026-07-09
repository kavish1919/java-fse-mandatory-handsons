package com.library.repository;

import com.library.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * BookRepository – Data-Access Layer.
 *
 * <p>Manages an in-memory catalogue of {@link Book} objects.
 * Spring creates ONE singleton instance and injects it into
 * every {@code BookService} bean, regardless of the DI style used.
 */
public class BookRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(BookRepository.class);

    /** In-memory store keyed by bookId. LinkedHashMap preserves insertion order. */
    private final Map<Integer, Book> store = new LinkedHashMap<>();

    /**
     * No-arg constructor – Spring calls this to instantiate the singleton bean.
     * Seeds the catalogue with sample data.
     */
    public BookRepository() {
        logger.info("BookRepository: instantiated by Spring IoC container.");
        store.put(1, new Book(1, "Clean Code",                 "Robert C. Martin",    "978-0132350884"));
        store.put(2, new Book(2, "The Pragmatic Programmer",   "Hunt & Thomas",       "978-0201616224"));
        store.put(3, new Book(3, "Design Patterns",            "Gang of Four",        "978-0201633610"));
        store.put(4, new Book(4, "Effective Java",             "Joshua Bloch",        "978-0134685991"));
        store.put(5, new Book(5, "Spring in Action",           "Craig Walls",         "978-1617294945"));
        logger.debug("BookRepository: {} books pre-loaded.", store.size());
    }

    /** Returns all books as an unmodifiable list. */
    public List<Book> findAll() {
        logger.debug("BookRepository.findAll() – {} books in catalogue.", store.size());
        return new ArrayList<>(store.values());
    }

    /**
     * Finds a book by its ID.
     *
     * @param bookId the identifier to look up
     * @return Optional containing the book, or empty if absent
     */
    public Optional<Book> findById(int bookId) {
        Book book = store.get(bookId);
        if (book != null) {
            logger.debug("BookRepository.findById({}) – found: {}", bookId, book.getTitle());
        } else {
            logger.warn("BookRepository.findById({}) – not found.", bookId);
        }
        return Optional.ofNullable(book);
    }

    /**
     * Adds a new book to the repository.
     *
     * @param book the book to add (must not be null)
     * @throws IllegalArgumentException if a book with the same ID already exists
     */
    public void save(Book book) {
        if (book == null) throw new IllegalArgumentException("Book must not be null.");
        if (store.containsKey(book.getBookId())) {
            throw new IllegalArgumentException(
                    "Book id " + book.getBookId() + " already exists.");
        }
        store.put(book.getBookId(), book);
        logger.info("BookRepository.save() – saved: {}", book.getTitle());
    }

    /** Returns the total number of books. */
    public int count() { return store.size(); }
}
