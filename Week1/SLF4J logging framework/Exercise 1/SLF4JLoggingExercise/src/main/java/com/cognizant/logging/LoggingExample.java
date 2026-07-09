package com.cognizant.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoggingExample – Exercise 1: Logging Error Messages and Warning Levels.
 *
 * <p>This class contains two sections:
 * <ol>
 *   <li><b>Exact solution</b> from the exercise specification –
 *       {@link #runExerciseSolution()} – calls {@code logger.error()} and
 *       {@code logger.warn()} verbatim as required.</li>
 *   <li><b>Extended demonstration</b> – shows all five SLF4J log levels and
 *       production best practices (parameterised messages, exception logging).</li>
 * </ol>
 *
 * <h2>SLF4J Log Levels (lowest → highest severity)</h2>
 * <pre>
 *   TRACE  – fine-grained diagnostic information (method entry/exit)
 *   DEBUG  – diagnostic information useful during development
 *   INFO   – general application lifecycle events
 *   WARN   – potentially harmful situation; application continues
 *   ERROR  – serious fault; operation may have failed
 * </pre>
 *
 * <p>The active level is controlled by {@code logback.xml}.
 * Messages below the configured threshold are discarded at zero cost
 * because SLF4J evaluates guard conditions lazily.
 */
public class LoggingExample {

    // -------------------------------------------------------------------------
    // Logger declaration – the standard SLF4J pattern.
    //   • private static final  → one instance shared across all method calls
    //   • LoggerFactory.getLogger(Class) → ties the logger name to this class
    // -------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    // =========================================================================
    // Entry point
    // =========================================================================

    public static void main(String[] args) {
        logger.info("=== SLF4J Logging Exercise 1 – START ===");

        // Part 1: exact solution from the exercise specification
        runExerciseSolution();

        // Part 2: extended demonstration
        runExtendedDemo();

        logger.info("=== SLF4J Logging Exercise 1 – END ===");
    }

    // =========================================================================
    // PART 1 – Exact solution code from the exercise specification
    // =========================================================================

    /**
     * Reproduces the exact {@code main} body from the exercise verbatim.
     */
    public static void runExerciseSolution() {
        logger.info("--- Part 1: Exercise solution (verbatim) ---");

        logger.error("This is an error message");
        logger.warn("This is a warning message");
    }

    // =========================================================================
    // PART 2 – Extended demonstration: all five levels + best practices
    // =========================================================================

    /**
     * Demonstrates all five SLF4J log levels and production best practices.
     */
    public static void runExtendedDemo() {
        logger.info("--- Part 2: All five log levels ---");

        // --- TRACE -----------------------------------------------------------
        // Finest granularity; used for method entry/exit or loop diagnostics.
        // Disabled in production (level threshold set to INFO or higher).
        logger.trace("TRACE: Entering runExtendedDemo() – most detailed level");

        // --- DEBUG -----------------------------------------------------------
        // Useful during development; shows variable states and decision points.
        int recordCount = 42;
        logger.debug("DEBUG: Processing {} records from the data source", recordCount);

        // --- INFO  -----------------------------------------------------------
        // Normal operational events: startup, shutdown, configuration loaded.
        String environment = "PRODUCTION";
        logger.info("INFO : Application running in {} environment", environment);

        // --- WARN  -----------------------------------------------------------
        // Something unexpected but recoverable; attention required.
        double diskUsagePct = 87.5;
        logger.warn("WARN : Disk usage at {}% – consider archiving old logs", diskUsagePct);

        // --- ERROR -----------------------------------------------------------
        // A significant failure; part of the operation did not complete.
        logger.error("ERROR: Failed to connect to the payment gateway");

        // --- Best Practice 1: Parameterised messages --------------------------
        // CORRECT – SLF4J defers toString() until the message is actually logged.
        // WRONG   – "User ID: " + userId  (string built even when level is disabled)
        logger.info("--- Best Practice 1: Parameterised messages ---");
        int userId = 1001;
        String action = "TRANSFER";
        double amount = 5000.00;
        logger.info("User {} performed action '{}' for amount ${}", userId, action, amount);

        // --- Best Practice 2: Exception logging --------------------------------
        // Always pass the Throwable as the LAST argument so Logback prints
        // the full stack trace automatically.
        logger.info("--- Best Practice 2: Exception logging with stack trace ---");
        try {
            riskyOperation();
        } catch (IllegalStateException ex) {
            // Correct: message + throwable → full stack trace printed
            logger.error("ERROR: riskyOperation() failed – {}", ex.getMessage(), ex);
        }

        // --- Best Practice 3: Conditional guard (isXxxEnabled) ----------------
        // When building the log argument is expensive, guard with isXxxEnabled().
        logger.info("--- Best Practice 3: isDebugEnabled() guard ---");
        if (logger.isDebugEnabled()) {
            // The expensive computation only runs if DEBUG is active
            String expensiveDiagnostic = buildDiagnosticReport();
            logger.debug("DEBUG: Diagnostic report → {}", expensiveDiagnostic);
        }

        logger.trace("TRACE: Exiting runExtendedDemo()");
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /** Simulates an operation that may fail. */
    private static void riskyOperation() {
        throw new IllegalStateException("Database connection pool exhausted");
    }

    /** Simulates building an expensive diagnostic string. */
    private static String buildDiagnosticReport() {
        return "heap=512MB, threads=48, connections=10/20, latency=23ms";
    }
}
