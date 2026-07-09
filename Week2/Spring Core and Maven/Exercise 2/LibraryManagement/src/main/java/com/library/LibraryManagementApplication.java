package com.library;

import com.library.model.Book;
import com.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * LibraryManagementApplication – Main class for Exercise 2.
 *
 * <p>Loads the Spring ApplicationContext and retrieves <em>both</em>
 * {@code BookService} beans to prove that both injection styles
 * are wired correctly and produce identical behaviour:
 *
 * <pre>
 *   bookService               → wired via SETTER injection
 *   bookServiceViaConstructor → wired via CONSTRUCTOR injection
 * </pre>
 *
 * <p>Both services delegate to the <em>same</em> singleton
 * {@code BookRepository} bean, so catalogue mutations made through
 * one service are visible through the other.
 */
public class LibraryManagementApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(LibraryManagementApplication.class);

    private static final String LINE = "─".repeat(60);

    public static void main(String[] args) {

        // ─── Step 1: Boot the Spring IoC container ───────────────────────────
        logger.info("Loading Spring ApplicationContext from applicationContext.xml...");
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("ApplicationContext loaded. Beans available: {}",
                (Object) context.getBeanDefinitionNames());

        // ─── Step 2: Retrieve BOTH BookService beans ──────────────────────────
        BookService setterService      = context.getBean("bookService",
                BookService.class);
        BookService constructorService = context.getBean("bookServiceViaConstructor",
                BookService.class);

        // ─── Step 3: Verify SETTER INJECTION ─────────────────────────────────
        System.out.println("\n" + LINE);
        System.out.println("  SETTER INJECTION  (bean id='bookService')");
        System.out.println(LINE);
        printCatalogue(setterService);

        // ─── Step 4: Verify CONSTRUCTOR INJECTION ─────────────────────────────
        System.out.println("\n" + LINE);
        System.out.println("  CONSTRUCTOR INJECTION  (bean id='bookServiceViaConstructor')");
        System.out.println(LINE);
        printCatalogue(constructorService);

        // ─── Step 5: Prove shared BookRepository singleton ────────────────────
        // Add a new book through the setter-injected service…
        System.out.println("\n" + LINE);
        System.out.println("  SHARED REPOSITORY PROOF");
        System.out.println(LINE);

        Book newBook = new Book(6, "Head First Design Patterns",
                "Freeman & Robson", "978-0596007126");
        System.out.println("Adding book via setterService: " + newBook.getTitle());
        setterService.addBook(newBook);

        // …and verify it is visible through the constructor-injected service
        System.out.println("\nRetrieving same book via constructorService (id=6):");
        System.out.println(constructorService.getBookById(6));

        System.out.printf("%nTotal books (via setter service)     : %d%n",
                setterService.getTotalBooks());
        System.out.printf("Total books (via constructor service) : %d%n",
                constructorService.getTotalBooks());
        System.out.println("Both counts are equal = " +
                (setterService.getTotalBooks() == constructorService.getTotalBooks()));

        // ─── Step 6: Graceful shutdown ─────────────────────────────────────────
        ((ClassPathXmlApplicationContext) context).close();
        logger.info("Spring context closed. Exercise 2 complete.");
    }

    /** Helper: prints the full catalogue from a BookService. */
    private static void printCatalogue(BookService service) {
        List<String> books = service.getAllBooks();
        books.forEach(System.out::println);
        System.out.println("Total: " + service.getTotalBooks() + " book(s)");
    }
}
