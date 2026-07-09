package com.library.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * BookRepository – in-memory data-access stub.
 * Demonstrates that {@code spring-context} is on the classpath and usable.
 */
public class BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

    private final List<String> books = Arrays.asList(
            "Clean Code – Robert C. Martin",
            "Effective Java – Joshua Bloch",
            "Spring in Action – Craig Walls"
    );

    public BookRepository() {
        logger.info("BookRepository instantiated.");
    }

    public List<String> findAll() {
        logger.debug("BookRepository.findAll() returning {} books", books.size());
        return books;
    }

    public int count() {
        return books.size();
    }
}
