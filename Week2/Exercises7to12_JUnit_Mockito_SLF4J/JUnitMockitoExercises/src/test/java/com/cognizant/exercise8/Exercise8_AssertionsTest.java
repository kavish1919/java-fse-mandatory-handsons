package com.cognizant.exercise8;

import com.cognizant.calculator.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 8 — JUnit 5 Assertions.
 *
 * <p>Demonstrates every major assertion type:
 * <ul>
 *   <li>{@code assertEquals} — exact value match</li>
 *   <li>{@code assertNotEquals} — value mismatch</li>
 *   <li>{@code assertTrue} / {@code assertFalse} — boolean conditions</li>
 *   <li>{@code assertNull} / {@code assertNotNull} — null checks</li>
 *   <li>{@code assertThrows} — exception behaviour</li>
 *   <li>{@code assertAll} — grouped assertions</li>
 *   <li>{@code assertDoesNotThrow} — absence of exception</li>
 *   <li>Parameterised tests with {@code @CsvSource}</li>
 * </ul>
 */
@DisplayName("Exercise 8 — JUnit 5 Assertions")
class Exercise8_AssertionsTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // ------------------------------------------------------------------
    // assertEquals / assertNotEquals
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertEquals: multiply(3, 4) == 12")
    void assertEqualsMultiply() {
        assertEquals(12.0, calculator.multiply(3, 4),
                "3 × 4 must equal 12.");
    }

    @Test
    @DisplayName("assertEquals with delta: floating-point division")
    void assertEqualsWithDelta() {
        // 1 / 3 has an infinite decimal expansion; use delta for comparison.
        assertEquals(0.333, calculator.divide(1, 3), 0.001,
                "1 ÷ 3 must be approximately 0.333.");
    }

    @Test
    @DisplayName("assertNotEquals: subtract(5,2) should not equal 10")
    void assertNotEqualsSubtract() {
        assertNotEquals(10.0, calculator.subtract(5, 2));
    }

    // ------------------------------------------------------------------
    // assertTrue / assertFalse
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertTrue: result of add(10, 5) is greater than 14")
    void assertTrueAddResult() {
        assertTrue(calculator.add(10, 5) > 14,
                "10 + 5 must be greater than 14.");
    }

    @Test
    @DisplayName("assertFalse: 7 is not even")
    void assertFalseIsEven() {
        assertFalse(calculator.isEven(7), "7 must not be even.");
    }

    @Test
    @DisplayName("assertTrue: 8 is even")
    void assertTrueIsEven() {
        assertTrue(calculator.isEven(8), "8 must be even.");
    }

    // ------------------------------------------------------------------
    // assertNull / assertNotNull
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertNull: maxOrNull(5, 5) returns null for equal values")
    void assertNullForEqualInputs() {
        assertNull(calculator.maxOrNull(5, 5),
                "maxOrNull must return null when both inputs are equal.");
    }

    @Test
    @DisplayName("assertNotNull: maxOrNull(3, 7) returns a non-null value")
    void assertNotNullForDifferentInputs() {
        assertNotNull(calculator.maxOrNull(3, 7),
                "maxOrNull must return a non-null value for unequal inputs.");
    }

    // ------------------------------------------------------------------
    // assertThrows
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertThrows: divide(10, 0) must throw ArithmeticException")
    void assertThrowsDivideByZero() {
        ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10, 0),
                "Dividing by zero must throw ArithmeticException.");

        assertEquals("Division by zero is undefined.", ex.getMessage());
    }

    @Test
    @DisplayName("assertThrows: isEven(3.5) must throw IllegalArgumentException")
    void assertThrowsIsEvenWithDecimal() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.isEven(3.5));
    }

    // ------------------------------------------------------------------
    // assertDoesNotThrow
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertDoesNotThrow: valid divide(10, 2) produces no exception")
    void assertDoesNotThrowValidDivide() {
        assertDoesNotThrow(() -> calculator.divide(10, 2));
    }

    // ------------------------------------------------------------------
    // assertAll (grouped assertions — all failures reported together)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("assertAll: verify multiple calculator operations at once")
    void assertAllCalculatorOperations() {
        assertAll("calculator operations",
                () -> assertEquals(7.0,  calculator.add(3, 4)),
                () -> assertEquals(1.0,  calculator.subtract(4, 3)),
                () -> assertEquals(12.0, calculator.multiply(3, 4)),
                () -> assertEquals(2.0,  calculator.divide(8, 4)),
                () -> assertEquals(5.0,  calculator.abs(-5))
        );
    }

    // ------------------------------------------------------------------
    // Parameterised tests
    // ------------------------------------------------------------------

    @ParameterizedTest(name = "add({0}, {1}) == {2}")
    @CsvSource({
        "1, 2, 3",
        "0, 0, 0",
        "-1, 1, 0",
        "100, 200, 300",
        "-50, -50, -100"
    })
    @DisplayName("Parameterised: add() for multiple input pairs")
    void parameterisedAdd(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), 1e-9);
    }
}
