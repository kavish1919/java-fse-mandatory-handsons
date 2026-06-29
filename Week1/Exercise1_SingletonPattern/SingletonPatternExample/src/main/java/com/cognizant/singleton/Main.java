package com.cognizant.singleton;

/**
 * Entry point demonstrating the Singleton Logger.
 *
 * <p>Demonstrates:
 * <ul>
 *   <li>Single-instance guarantee via reference equality.</li>
 *   <li>All log levels in action.</li>
 *   <li>Null / blank message edge-case handling.</li>
 * </ul>
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Singleton Pattern Demo — Logger ===\n");

        // Obtain two references — both must point to the exact same object.
        Logger loggerA = Logger.getInstance();
        Logger loggerB = Logger.getInstance();

        // ---------------------------------------------------------------
        // Identity test (== checks object reference, not value equality)
        // ---------------------------------------------------------------
        System.out.println("loggerA == loggerB : " + (loggerA == loggerB));
        System.out.println("hashCode A         : " + System.identityHashCode(loggerA));
        System.out.println("hashCode B         : " + System.identityHashCode(loggerB));
        System.out.println();

        // ---------------------------------------------------------------
        // Functional demonstration
        // ---------------------------------------------------------------
        loggerA.log("Application started successfully.");
        loggerA.log(Logger.Level.DEBUG, "Debug mode enabled — verbose output active.");
        loggerB.log(Logger.Level.WARN,  "Configuration file not found; using defaults.");
        loggerB.log(Logger.Level.ERROR, "Failed to connect to database on startup.");

        // Edge-case: null / blank message
        loggerA.log(null);
        loggerA.log("   ");

        // Exception logging
        try {
            int result = 10 / 0;
        } catch (ArithmeticException ex) {
            loggerA.logException("Unexpected arithmetic error during calculation", ex);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
