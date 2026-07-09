package com.library;

import com.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * LibraryManagementApp – Main class that bootstraps the Spring IoC container
 * and exercises the {@link BookService} bean.
 *
 * <h2>How Spring IoC works here</h2>
 * <ol>
 *   <li>{@code new ClassPathXmlApplicationContext("applicationContext.xml")}
 *       reads the XML file from the classpath.</li>
 *   <li>Spring instantiates all beans declared in the XML:
 *       <ul>
 *         <li>{@code BookRepository} – no-arg constructor called</li>
 *         <li>{@code BookService}    – no-arg constructor called, then
 *             {@code setBookRepository()} invoked with the repository bean</li>
 *       </ul>
 *   </li>
 *   <li>{@code context.getBean("bookService", BookService.class)} returns
 *       the fully wired singleton from the container.</li>
 *   <li>The application uses {@code bookService} without knowing how its
 *       dependencies were assembled – the container handles everything.</li>
 * </ol>
 */
public class LibraryManagementApp {

    private static final Logger logger =
            LoggerFactory.getLogger(LibraryManagementApp.class);

    public static void main(String[] args) {

        logger.info("╔══════════════════════════════════════════════════════╗");
        logger.info("║   Library Management System – Spring IoC Demo        ║");
        logger.info("╚══════════════════════════════════════════════════════╝");

        // ─────────────────────────────────────────────────────────────────────
        // Step 1: Load the Spring ApplicationContext from XML
        //         ClassPathXmlApplicationContext searches the classpath for
        //         the named file (src/main/resources is on the classpath).
        // ─────────────────────────────────────────────────────────────────────
        logger.info("Loading Spring ApplicationContext from applicationContext.xml ...");

        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        logger.info("Spring ApplicationContext loaded successfully.");

        // ─────────────────────────────────────────────────────────────────────
        // Step 2: Retrieve the BookService bean from the container.
        //         Spring has already injected BookRepository into it.
        // ─────────────────────────────────────────────────────────────────────
        BookService bookService = context.getBean("bookService", BookService.class);
        logger.info("BookService bean retrieved from Spring context.");

        // ─────────────────────────────────────────────────────────────────────
        // Step 3: Exercise the service – shows DI is working end-to-end
        // ─────────────────────────────────────────────────────────────────────

        // 3a: List all books
        System.out.println("\n--- All Books in the Library ---");
        List<String> allBooks = bookService.getAllBooks();
        allBooks.forEach(System.out::println);

        // 3b: Total book count
        System.out.println("\n--- Total Books ---");
        System.out.println("Total books in library: " + bookService.getTotalBooks());

        // 3c: Find a specific book (hit)
        System.out.println("\n--- Search by ID: Book #3 ---");
        System.out.println(bookService.getBookById(3));

        // 3d: Find a non-existent book (miss)
        System.out.println("\n--- Search by ID: Book #99 (not found) ---");
        System.out.println(bookService.getBookById(99));

        // 3e: Add a new book
        System.out.println("\n--- Adding a New Book ---");
        bookService.addBook(6, "Head First Design Patterns – Freeman & Robson");
        System.out.println("After adding: total = " + bookService.getTotalBooks() + " books");

        // 3f: Verify the newly added book is retrievable
        System.out.println("\n--- Search for Newly Added Book #6 ---");
        System.out.println(bookService.getBookById(6));

        // ─────────────────────────────────────────────────────────────────────
        // Step 4: Close the ApplicationContext to trigger bean destroy callbacks
        // ─────────────────────────────────────────────────────────────────────
        ((ClassPathXmlApplicationContext) context).close();
        logger.info("Spring ApplicationContext closed. Application exiting.");
    }
}
