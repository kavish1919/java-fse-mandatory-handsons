package com.cognizant.exercise7;

import com.cognizant.calculator.Calculator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 7 — JUnit 5 Setup and First Tests.
 *
 * <p>Demonstrates:
 * <ul>
 *   <li>JUnit 5 dependency (verified by running)</li>
 *   <li>{@code @Test}, {@code @BeforeAll}, {@code @AfterAll}</li>
 *   <li>Basic {@code assertEquals} and {@code assertNotNull}</li>
 *   <li>A clearly named green build</li>
 * </ul>
 */
@DisplayName("Exercise 7 — JUnit 5 Setup: First Tests")
class Exercise7_JUnitSetupTest {

    private Calculator calculator;

    @BeforeAll
    static void suiteSetup() {
        System.out.println("[BeforeAll] Test suite initialising — JUnit 5 is configured correctly.");
    }

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @AfterEach
    void tearDown() {
        calculator = null;  // release reference after each test
    }

    @AfterAll
    static void suiteTeardown() {
        System.out.println("[AfterAll] Test suite complete.");
    }

    // ------------------------------------------------------------------

    @Test
    @DisplayName("JUnit 5 is reachable — this test verifies the dependency is wired")
    void junitIsAvailable() {
        // If this test runs, JUnit 5 is correctly configured on the classpath.
        assertTrue(true, "JUnit 5 is available and working.");
    }

    @Test
    @DisplayName("Calculator is instantiated by @BeforeEach")
    void calculatorIsNotNull() {
        assertNotNull(calculator, "Calculator must be initialised before each test.");
    }

    @Test
    @DisplayName("add(2, 3) should equal 5")
    void addTwoPlusThreeEqualsFive() {
        double result = calculator.add(2, 3);
        assertEquals(5.0, result, "2 + 3 must equal 5.");
    }

    @Test
    @DisplayName("subtract(10, 4) should equal 6")
    void subtractTenMinusFourEqualsSix() {
        assertEquals(6.0, calculator.subtract(10, 4));
    }
}
