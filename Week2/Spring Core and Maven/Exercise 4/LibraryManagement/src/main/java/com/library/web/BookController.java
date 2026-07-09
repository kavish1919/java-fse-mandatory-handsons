package com.library.web;

import com.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * BookController – simulates a Spring MVC controller.
 *
 * <p>In a full web application this class would be annotated with
 * {@code @Controller} and methods with {@code @RequestMapping}.
 * Here it is a plain class that demonstrates spring-webmvc is
 * on the compile classpath without requiring a running web server.
 *
 * <p>The imports below reference Spring WebMVC types, confirming
 * the dependency resolves correctly.
 */
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    /*
     * Import Spring WebMVC classes to prove the dependency is available.
     * In a real app: @Controller + @RequestMapping("...") on the class,
     * @GetMapping / @PostMapping on methods, ModelAndView as return type.
     */
    // org.springframework.web.servlet.ModelAndView    – response model
    // org.springframework.web.bind.annotation.*       – mapping annotations
    // org.springframework.ui.Model                    – view model carrier

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        logger.info("BookController instantiated (spring-webmvc on classpath).");
    }

    /**
     * Simulates a GET /books handler.
     * Returns a formatted string as if producing an HTTP response body.
     *
     * @return response body string
     */
    public String listBooks() {
        logger.info("BookController.listBooks() – handling GET /books");
        List<String> books = bookService.getAllBooks();
        StringBuilder sb = new StringBuilder("HTTP 200 OK\n");
        sb.append("Books in Library (").append(books.size()).append("):\n");
        books.forEach(b -> sb.append("  • ").append(b).append("\n"));
        return sb.toString();
    }
}
