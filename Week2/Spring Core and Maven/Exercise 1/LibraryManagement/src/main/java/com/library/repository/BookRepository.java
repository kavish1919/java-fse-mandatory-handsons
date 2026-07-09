package com.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * BookRepository – Data-Access Layer.
 *
 * <p>Simulates a persistence store using an in-memory {@link Map}.
 * In a production application this class would be replaced by a JPA
 * repository or JDBC template backed by a real database.
 *
 * <p>Spring manages this class as a <em>singleton bean</em> defined in
 * {@code applicationContext.xml}; the IoC container creates one instance
 * and injects it wherever referenced.
 */
public class BookRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(BookRepository.class);

    /** In-memory data store: bookId → title */
    private final Map<Integer, String> bookStore = new HashMap<>();

    /**
     * Default constructor called by Spring when it instantiates the bean.
     * Pre-loads a small catalogue to demonstrate the application.
     */
    public BookRepository() {
        logger.info("BookRepository bean instantiated by Spring IoC container.");
        // Seed data
        bookStore.put(1, "Clean Code – Robert C. Martin");
        bookStore.put(2, "The Pragmatic Programmer – Hunt & Thomas");
        bookStore.put(3, "Design Patterns – Gang of Four");
        bookStore.put(4, "Effective Java – Joshua Bloch");
        bookStore.put(5, "Spring in Action – Craig Walls");
    }

    /**
     * Returns all books in the repository.
     *
     * @return a new list containing every book entry (id, title)
     */
    public List<String> findAll() {
        logger.debug("BookRepository.findAll() – returning {} books", bookStore.size());
        List<String> result = new ArrayList<>();
        bookStore.forEach((id, title) ->
                result.add(String.format("  [%d] %s", id, title)));
        return result;
    }

    /**
     * Finds a book by its identifier.
     *
     * @param bookId the book's unique identifier
     * @return an {@link Optional} containing the title, or empty if not found
     */
    public Optional<String> findById(int bookId) {
        String title = bookStore.get(bookId);
        if (title != null) {
            logger.debug("BookRepository.findById({}) – found: '{}'", bookId, title);
        } else {
            logger.warn("BookRepository.findById({}) – book not found", bookId);
        }
        return Optional.ofNullable(title);
    }

    /**
     * Saves a new book to the repository.
     *
     * @param bookId unique identifier for the new book
     * @param title  book title (must not be blank)
     * @throws IllegalArgumentException if title is blank or id already exists
     */
    public void save(int bookId, String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title must not be blank.");
        }
        if (bookStore.containsKey(bookId)) {
            throw new IllegalArgumentException("Book with id " + bookId + " already exists.");
        }
        bookStore.put(bookId, title);
        logger.info("BookRepository.save() – saved: [{}] '{}'", bookId, title);
    }

    /**
     * Returns the total number of books in the repository.
     */
    public int count() {
        return bookStore.size();
    }
}
