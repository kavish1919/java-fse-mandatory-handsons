package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * BookService – Business-Logic Layer.
 *
 * <p>Supports <strong>both</strong> Dependency Injection styles that Spring
 * can use depending on how the bean is configured in
 * {@code applicationContext.xml}:
 *
 * <h2>1 · Setter Injection (bean id="bookService")</h2>
 * <ol>
 *   <li>Spring calls: {@code new BookService()}  (no-arg constructor)</li>
 *   <li>Spring calls: {@code setBookRepository(repo)}  (setter)</li>
 *   <li>The repository is now available for use.</li>
 * </ol>
 * XML: {@code <property name="bookRepository" ref="bookRepository"/>}
 *
 * <h2>2 · Constructor Injection (bean id="bookServiceViaConstructor")</h2>
 * <ol>
 *   <li>Spring calls: {@code new BookService(repo)}  (constructor with arg)</li>
 *   <li>The repository is available immediately — no setter needed.</li>
 * </ol>
 * XML: {@code <constructor-arg ref="bookRepository"/>}
 *
 * <h2>Comparison</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Criteria</th><th>Setter Injection</th><th>Constructor Injection</th></tr>
 *   <tr><td>When injected</td><td>After construction</td><td>During construction</td></tr>
 *   <tr><td>Optional deps</td><td>✔ (can remain null)</td><td>✘ (must provide)</td></tr>
 *   <tr><td>Immutability</td><td>✘ (field can change)</td><td>✔ (field can be final)</td></tr>
 *   <tr><td>Circular deps</td><td>✔ Spring resolves them</td><td>✘ Runtime error</td></tr>
 * </table>
 */
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    // ─── field: final when constructor-injected, non-final for setter injection ───
    private BookRepository bookRepository;

    // =========================================================================
    // No-arg constructor — required for SETTER INJECTION
    // =========================================================================

    /**
     * No-arg constructor called by Spring when using setter injection.
     * After this, Spring calls {@link #setBookRepository(BookRepository)}.
     */
    public BookService() {
        logger.info("BookService: instantiated via no-arg constructor (setter injection path).");
    }

    // =========================================================================
    // Parameterised constructor — used for CONSTRUCTOR INJECTION
    // =========================================================================

    /**
     * Constructor called by Spring when using constructor injection.
     * The repository is immediately available — no setter call needed.
     *
     * @param bookRepository the repository bean injected by Spring
     */
    public BookService(BookRepository bookRepository) {
        if (bookRepository == null) {
            throw new IllegalArgumentException("BookRepository must not be null.");
        }
        this.bookRepository = bookRepository;
        logger.info("BookService: instantiated via constructor injection — repository wired.");
    }

    // =========================================================================
    // Setter — called by Spring after no-arg construction (SETTER INJECTION)
    // =========================================================================

    /**
     * Spring calls this method to inject {@link BookRepository} when
     * {@code <property name="bookRepository" ref="bookRepository"/>}
     * is declared in {@code applicationContext.xml}.
     *
     * <p>The method name <em>must</em> follow the JavaBeans convention:
     * {@code set} + capitalised property name → {@code setBookRepository}.
     *
     * @param bookRepository the Spring-managed repository bean
     */
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        logger.info("BookService: setBookRepository() called — setter injection complete.");
    }

    // =========================================================================
    // Business-logic methods (identical regardless of injection style)
    // =========================================================================

    /**
     * Returns a formatted list of all books in the library.
     *
     * @return list of display strings, one per book
     */
    public List<String> getAllBooks() {
        logger.info("BookService.getAllBooks() invoked.");
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(b -> String.format("  [%d] %-40s by %s  (ISBN: %s)",
                        b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn()))
                .toList();
    }

    /**
     * Retrieves a book by ID and returns a human-readable message.
     *
     * @param bookId the book identifier
     * @return a description of the found book, or a "not found" message
     */
    public String getBookById(int bookId) {
        logger.info("BookService.getBookById({}) invoked.", bookId);
        Optional<Book> result = bookRepository.findById(bookId);
        return result
                .map(b -> "Found → " + b)
                .orElse("No book found with id=" + bookId);
    }

    /**
     * Adds a new book to the library.
     *
     * @param book the book to add
     */
    public void addBook(Book book) {
        logger.info("BookService.addBook() – adding '{}'", book.getTitle());
        bookRepository.save(book);
    }

    /** @return total number of books currently in the library */
    public int getTotalBooks() {
        return bookRepository.count();
    }
}
