package com.cognizant.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger – Thread-safe Singleton logging utility.
 *
 * <p>Design pattern: Singleton (Bill Pugh / Initialization-On-Demand Holder idiom).
 * This approach is lazy, thread-safe, and requires no synchronisation overhead
 * after the class is initialised by the JVM.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Guarantee a single shared Logger instance per JVM lifecycle.</li>
 *   <li>Expose INFO, WARN, and ERROR severity levels.</li>
 *   <li>Format every log entry with an ISO-style timestamp and severity label.</li>
 * </ul>
 */
public final class Logger {

    // -----------------------------------------------------------------------
    // Singleton infrastructure (Bill Pugh Holder idiom)
    // -----------------------------------------------------------------------

    /**
     * Private constructor – prevents external and subclass instantiation.
     */
    private Logger() {
        // Guard against reflection-based instantiation
        if (LoggerHolder.INSTANCE != null) {
            throw new IllegalStateException(
                    "Use Logger.getInstance() – reflection-based instantiation is not permitted.");
        }
    }

    /**
     * Inner static holder class.
     * The JVM initialises this class (and creates {@code INSTANCE}) only when
     * {@link #getInstance()} is called for the first time, providing lazy
     * initialisation that is inherently thread-safe.
     */
    private static final class LoggerHolder {
        private static final Logger INSTANCE = new Logger();
    }

    /**
     * Returns the single shared {@code Logger} instance.
     *
     * @return the global Logger singleton
     */
    public static Logger getInstance() {
        return LoggerHolder.INSTANCE;
    }

    // -----------------------------------------------------------------------
    // Logging API
    // -----------------------------------------------------------------------

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message at INFO severity.
     *
     * @param message the message to log
     */
    public void info(String message) {
        log("INFO ", message);
    }

    /**
     * Logs a message at WARN severity.
     *
     * @param message the message to log
     */
    public void warn(String message) {
        log("WARN ", message);
    }

    /**
     * Logs a message at ERROR severity.
     *
     * @param message the message to log
     */
    public void error(String message) {
        log("ERROR", message);
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    /**
     * Formats and prints a log entry to {@code System.out}.
     *
     * @param level   severity label (INFO / WARN / ERROR)
     * @param message the log message
     */
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] [Logger@%s] %s%n",
                timestamp,
                level,
                Integer.toHexString(System.identityHashCode(this)),
                message);
    }
}
