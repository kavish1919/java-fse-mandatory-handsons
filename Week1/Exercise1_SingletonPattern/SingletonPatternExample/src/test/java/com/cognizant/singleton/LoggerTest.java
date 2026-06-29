package com.cognizant.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for the {@link Logger} Singleton.
 *
 * <p>Covers:
 * <ul>
 *   <li>Single-instance guarantee (positive)</li>
 *   <li>Reference equality across multiple calls</li>
 *   <li>Null message edge-case (negative)</li>
 *   <li>Reflection-based instantiation rejection (negative)</li>
 * </ul>
 */
@DisplayName("Logger Singleton Tests")
class LoggerTest {

    // ------------------------------------------------------------------
    // Positive Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getInstance() must always return the identical object reference")
    void testSingletonInstanceIsSame() {
        Logger first  = Logger.getInstance();
        Logger second = Logger.getInstance();

        assertSame(first, second,
                "Two calls to getInstance() must return the exact same object.");
    }

    @Test
    @DisplayName("Multiple calls must return the same hashCode")
    void testSingletonHashCodeIsConsistent() {
        int hashA = System.identityHashCode(Logger.getInstance());
        int hashB = System.identityHashCode(Logger.getInstance());

        assertEquals(hashA, hashB,
                "Identity hash codes must be equal for a singleton.");
    }

    @Test
    @DisplayName("log(String) must not throw for a valid message")
    void testLogDoesNotThrowForValidMessage() {
        Logger logger = Logger.getInstance();
        assertDoesNotThrow(() -> logger.log("Valid log message."));
    }

    @Test
    @DisplayName("log(Level, String) must not throw for all levels")
    void testAllLevelsDoNotThrow() {
        Logger logger = Logger.getInstance();
        for (Logger.Level level : Logger.Level.values()) {
            assertDoesNotThrow(() -> logger.log(level, "Testing level: " + level));
        }
    }

    // ------------------------------------------------------------------
    // Edge / Negative Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("log() with null message must not propagate an NPE")
    void testLogNullMessageIsSafe() {
        Logger logger = Logger.getInstance();
        assertDoesNotThrow(() -> logger.log(null),
                "Null message must be handled gracefully without throwing.");
    }

    @Test
    @DisplayName("log() with blank message must not propagate an exception")
    void testLogBlankMessageIsSafe() {
        Logger logger = Logger.getInstance();
        assertDoesNotThrow(() -> logger.log("   "));
    }

    @Test
    @DisplayName("Reflection must not produce a second Logger instance")
    void testReflectionCannotBreakSingleton() throws Exception {
        java.lang.reflect.Constructor<Logger> constructor =
                Logger.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThrows(Exception.class, constructor::newInstance,
                "Reflective instantiation must throw because the singleton guard " +
                "rejects a second construction attempt.");
    }
}
