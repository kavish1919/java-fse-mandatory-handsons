package com.cognizant.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoggerSingletonTest – JUnit 5 test suite verifying the Singleton contract.
 *
 * <p>Test cases:
 * <ol>
 *   <li>Single sequential call returns a non-null instance.</li>
 *   <li>Two sequential calls return the same object reference.</li>
 *   <li>Multiple sequential calls all return the same reference.</li>
 *   <li>Concurrent calls from 20 threads all receive the same instance.</li>
 *   <li>Logger methods (info/warn/error) execute without throwing exceptions.</li>
 * </ol>
 */
class LoggerSingletonTest {

    // -----------------------------------------------------------------------
    // TC-01: getInstance() must not return null
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-01: getInstance() returns a non-null instance")
    void testGetInstanceIsNotNull() {
        Logger logger = Logger.getInstance();
        assertNotNull(logger, "Logger.getInstance() must never return null.");
    }

    // -----------------------------------------------------------------------
    // TC-02: Two calls return the same reference
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-02: Two sequential calls return the identical object reference")
    void testSameReferenceOnTwoCalls() {
        Logger first  = Logger.getInstance();
        Logger second = Logger.getInstance();

        assertSame(first, second,
                "Expected the same Logger instance on repeated calls, but got different objects.");
    }

    // -----------------------------------------------------------------------
    // TC-03: Multiple sequential calls all return the same reference
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-03: Multiple sequential calls all share one instance")
    void testMultipleSequentialCallsReturnSameInstance() {
        Logger ref = Logger.getInstance();
        for (int i = 0; i < 100; i++) {
            assertSame(ref, Logger.getInstance(),
                    "Iteration " + i + ": getInstance() returned a different instance.");
        }
    }

    // -----------------------------------------------------------------------
    // TC-04: Thread-safety – 20 concurrent threads all get the same instance
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-04: 20 concurrent threads all receive the same singleton instance")
    void testSingletonUnderConcurrentAccess() throws Exception {
        final int THREAD_COUNT = 20;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Each thread fetches the Logger instance and returns its identity hash code
        Callable<Integer> task = () -> System.identityHashCode(Logger.getInstance());

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(executor.submit(task));
        }
        executor.shutdown();

        // Collect all identity hash codes into a Set; a true singleton produces a set of size 1
        Set<Integer> identityHashes = new HashSet<>();
        for (Future<Integer> future : futures) {
            identityHashes.add(future.get());
        }

        assertEquals(1, identityHashes.size(),
                "Expected exactly 1 unique instance across " + THREAD_COUNT
                + " threads, but found: " + identityHashes.size());
    }

    // -----------------------------------------------------------------------
    // TC-05: Logging methods do not throw exceptions
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("TC-05: info(), warn(), and error() execute without exceptions")
    void testLoggingMethodsDoNotThrow() {
        Logger logger = Logger.getInstance();
        assertDoesNotThrow(() -> logger.info("Info-level test message."),
                "logger.info() must not throw any exception.");
        assertDoesNotThrow(() -> logger.warn("Warn-level test message."),
                "logger.warn() must not throw any exception.");
        assertDoesNotThrow(() -> logger.error("Error-level test message."),
                "logger.error() must not throw any exception.");
    }
}
