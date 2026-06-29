package com.cognizant.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe Singleton Logger using the double-checked locking idiom.
 *
 * <p>Design Decisions:
 * <ul>
 *   <li>Private constructor prevents direct instantiation.</li>
 *   <li>{@code volatile} keyword ensures the JVM does not reorder instructions
 *       during instance creation, preventing a partially constructed object
 *       from being returned to a second thread.</li>
 *   <li>Double-checked locking avoids the overhead of synchronisation on every
 *       {@code getInstance()} call after the instance is created.</li>
 * </ul>
 *
 * <p>Complexity: Time O(1) amortised, Space O(1).
 */
public final class Logger {

    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** The single instance — volatile guarantees visibility across threads. */
    private static volatile Logger instance;

    /** Log-level constants aligned with common logging frameworks. */
    public enum Level { INFO, DEBUG, WARN, ERROR }

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /** Prevents external instantiation and reflection-based attacks. */
    private Logger() {
        if (instance != null) {
            throw new IllegalStateException(
                    "Use Logger.getInstance() — direct instantiation is forbidden.");
        }
    }

    // -------------------------------------------------------------------------
    // Singleton accessor
    // -------------------------------------------------------------------------

    /**
     * Returns the single application-wide {@link Logger} instance.
     *
     * <p>Thread safety is achieved without synchronising every call:
     * the outer null-check avoids locking once the instance exists;
     * the inner null-check inside the synchronized block handles the rare
     * race condition where two threads simultaneously pass the outer check.
     *
     * @return the shared {@code Logger} instance
     */
    public static Logger getInstance() {
        if (instance == null) {                       // first check (unsynchronised, fast path)
            synchronized (Logger.class) {
                if (instance == null) {               // second check (synchronised, safe path)
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Logs a message at {@link Level#INFO}.
     *
     * @param message the text to record
     */
    public void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Logs a message at the specified level.
     *
     * @param level   severity of the log entry
     * @param message the text to record
     */
    public void log(Level level, String message) {
        if (message == null || message.isBlank()) {
            log(Level.WARN, "Attempted to log a null or blank message.");
            return;
        }
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        System.out.printf("[%s] [%-5s] %s%n", timestamp, level, message);
    }

    /**
     * Logs an exception with its stack trace at {@link Level#ERROR}.
     *
     * @param message contextual description
     * @param throwable the caught exception
     */
    public void logException(String message, Throwable throwable) {
        log(Level.ERROR, message + " — " + throwable.getClass().getSimpleName()
                + ": " + throwable.getMessage());
    }
}
