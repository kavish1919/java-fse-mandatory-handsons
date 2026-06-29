package com.cognizant.exercise12;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 12 — SLF4J Logging.
 *
 * <p>Demonstrates all five SLF4J log levels: TRACE, DEBUG, INFO, WARN, ERROR.
 *
 * <p>Key practices demonstrated:
 * <ul>
 *   <li>Logger declared {@code private static final} — one instance per class.</li>
 *   <li>Parameterised messages ({}) avoid string concatenation overhead when the
 *       level is disabled — the string is only built if the level is enabled.</li>
 *   <li>Exception objects are passed as the last argument so SLF4J attaches the
 *       full stack trace without explicit {@code e.getMessage()} calls.</li>
 *   <li>No {@code System.out.println()} is used for application output.</li>
 * </ul>
 */
public class LoggingDemo {

    private static final Logger log = LoggerFactory.getLogger(LoggingDemo.class);

    public void demonstrateAllLevels() {
        log.trace("TRACE — finest detail; typically only during deep debugging sessions.");
        log.debug("DEBUG — diagnostic information useful during development.");
        log.info("INFO  — significant application event: service started on port {}.", 8080);
        log.warn("WARN  — recoverable issue: configuration key '{}' not found; using default.", "db.pool.size");
        log.error("ERROR — non-recoverable failure: failed to connect to '{}'.", "db-host:5432");
    }

    public void demonstrateParameterisedLogging(String userId, double amount) {
        // SLF4J resolves {} placeholders only if the level is active — zero cost when disabled.
        log.info("Processing payment of £{} for user '{}'.", amount, userId);
        log.debug("Payment breakdown — userId={}, amount={}, currency=GBP", userId, amount);
    }

    public void demonstrateExceptionLogging() {
        try {
            String value = null;
            int length = value.length();   // deliberately triggers NullPointerException
        } catch (NullPointerException ex) {
            // Pass exception as the LAST argument — SLF4J prints the full stack trace.
            log.error("Unexpected null encountered while processing user input.", ex);
        }

        try {
            int result = Integer.parseInt("not-a-number");
        } catch (NumberFormatException ex) {
            log.warn("Invalid numeric input received from user; defaulting to 0.", ex);
        }
    }

    public void demonstrateConditionalLogging() {
        // Guard with isDebugEnabled() only when building the debug message
        // is itself expensive (e.g. serialising a large object).
        if (log.isDebugEnabled()) {
            log.debug("Expensive debug payload: {}", buildExpensiveDebugString());
        }
    }

    /** Simulates a costly string-build operation. */
    private String buildExpensiveDebugString() {
        return "DetailedState{items=[...1000 items...]}";
    }

    public static void main(String[] args) {
        LoggingDemo demo = new LoggingDemo();

        log.info("=== Exercise 12: SLF4J Logging Demo ===");

        demo.demonstrateAllLevels();
        demo.demonstrateParameterisedLogging("user-7842", 149.99);
        demo.demonstrateExceptionLogging();
        demo.demonstrateConditionalLogging();

        log.info("=== Demo Complete ===");
    }
}
