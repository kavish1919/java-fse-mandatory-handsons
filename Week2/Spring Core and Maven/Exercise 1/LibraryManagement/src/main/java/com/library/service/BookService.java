package com.library.service;

import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * BookService – Business-Logic Layer.
 *
 * <p>Orchestrates calls to {@link BookRepository} and applies any business
 * rules (e.g., validation, formatting) before returning results to the
 * presentation layer.
 *
 * <p>Spring wires this class as a singleton bean and injects
 * {@link BookRepository} via the {@link #setBookRepository(BookRepository)}
 * setter, as declared in {@code applicationContext.xml}:
 * <pre>
 *   {@code <property name="bookRepository" ref="bookRepository"/>}
 * </pre>
 *
 * <h2>Dependency Injection (DI) – Setter Injection</h2>
 * <ol>
 *   <li>Spring instantiates {@code BookService} using the no-arg constructor.</li>
 *   <li>Spring calls {@code setBookRepository(bookRepository bean)}
 *       to inject the already-created repository instance.</li>
 *   <li>The service is now fully wired and ready to be used.</li>
 * </ol>
 */
public class BookService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookService.class);

    /**
     * Injected by Spring via setter injection.
     * Must have a public setter to allow Spring to inject the dependency.
     */
    private BookRepository bookRepository;

    /**
     * No-arg constructor – required by Spring for setter injection.
     */
    public BookService() {
        logger.info("BookService bean instantiated by Spring IoC container.");
    }

    // -------------------------------------------------------------------------
    // Setter – called by Spring to inject the BookRepository bean
    // -------------------------------------------------------------------------

    /**
     * Spring calls this method to inject the {@link BookRepository} dependency,
     * as configured in {@code applicationContext.xml}.
     *
     * @param bookRepository the repository bean managed by Spring
     */
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        logger.info("BookService: BookRepository injected via setter (DI complete).");
    }

    // -------------------------------------------------------------------------
    // Business-logic methods
    // -------------------------------------------------------------------------

    /**
     * Retrieves and displays all books in the library catalogue.
     *
     * @return list of formatted book entries
     */
    public List<String> getAllBooks() {
        logger.info("BookService.getAllBooks() – fetching full catalogue.");
        List<String> books = bookRepository.findAll();
        logger.debug("BookService.getAllBooks() – {} books retrieved.", books.size());
        return books;
    }

    /**
     * Retrieves a single book by ID, returning a descriptive message.
     *
     * @param bookId the book identifier
     * @return a human-readable description of the book, or a "not found" message
     */
    public String getBookById(int bookId) {
        logger.info("BookService.getBookById({}) – looking up book.", bookId);
        Optional<String> result = bookRepository.findById(bookId);
        return result
                .map(title -> "Found: [" + bookId + "] " + title)
                .orElse("Book with id " + bookId + " was not found in the library.");
    }

    /**
     * Adds a new book to the library.
     *
     * @param bookId unique identifier for the new book
     * @param title  book title
     */
    public void addBook(int bookId, String title) {
        logger.info("BookService.addBook() – adding book [{}] '{}'", bookId, title);
        bookRepository.save(bookId, title);
        logger.info("BookService.addBook() – successfully added.");
    }

    /**
     * Returns the total number of books currently in the library.
     *
     * @return book count
     */
    public int getTotalBooks() {
        int count = bookRepository.count();
        logger.info("BookService.getTotalBooks() – total: {}", count);
        return count;
    }
}
