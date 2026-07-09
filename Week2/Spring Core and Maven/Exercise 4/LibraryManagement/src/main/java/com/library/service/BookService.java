package com.library.service;

import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * BookService – business-logic layer.
 * Demonstrates Spring AOP proxy-readiness (implements no interface
 * intentionally so Spring uses CGLIB subclass proxy).
 */
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;

    public BookService() {
        logger.info("BookService instantiated (no-arg constructor).");
    }

    /** Setter used by Spring for Dependency Injection. */
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        logger.info("BookService: BookRepository injected via setter.");
    }

    public List<String> getAllBooks() {
        logger.info("BookService.getAllBooks() invoked.");
        return bookRepository.findAll();
    }

    public int getTotalBooks() {
        return bookRepository.count();
    }
}
