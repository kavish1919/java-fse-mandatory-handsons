package com.cognizant.exercise9;

import com.cognizant.calculator.Calculator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 9 — The AAA (Arrange / Act / Assert) Pattern.
 *
 * <p>Every test is explicitly structured into three named sections
 * using blank-line separation and inline comments, making the intent
 * immediately clear to reviewers.
 *
 * <p>Demonstrates:
 * <ul>
 *   <li>Strict AAA structure for every test method</li>
 *   <li>{@code @BeforeEach} / {@code @AfterEach} for fixture lifecycle</li>
 *   <li>Shared state initialised in {@code @BeforeEach} (Arrange phase lifted)</li>
 *   <li>Negative tests following the same AAA discipline</li>
 * </ul>
 */
@DisplayName("Exercise 9 — AAA Pattern")
class Exercise9_AAAPatternTest {

    // Shared fixture — initialised before every test.
    private Calculator calculator;

    /** Arrange (shared): create a fresh Calculator before each test. */
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    /** Teardown: release resources after each test. */
    @AfterEach
    void tearDown() {
        System.out.printf("[AfterEach] Test '%s' complete.%n",
                org.junit.jupiter.api.TestInfo.class.getSimpleName());
        calculator = null;
    }

    // ------------------------------------------------------------------
    // Positive AAA Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("AAA: add two positive numbers")
    void addTwoPositiveNumbers() {
        // Arrange
        double a = 15.0;
        double b = 25.0;

        // Act
        double result = calculator.add(a, b);

        // Assert
        assertEquals(40.0, result, "15 + 25 must equal 40.");
    }

    @Test
    @DisplayName("AAA: subtract yields a negative result")
    void subtractYieldsNegativeResult() {
        // Arrange
        double minuend    = 3.0;
        double subtrahend = 10.0;

        // Act
        double result = calculator.subtract(minuend, subtrahend);

        // Assert
        assertTrue(result < 0, "3 - 10 must produce a negative result.");
        assertEquals(-7.0, result);
    }

    @Test
    @DisplayName("AAA: divide produces correct quotient")
    void divideProducesCorrectQuotient() {
        // Arrange
        double dividend = 100.0;
        double divisor  = 4.0;

        // Act
        double result = calculator.divide(dividend, divisor);

        // Assert
        assertEquals(25.0, result, "100 ÷ 4 must equal 25.");
        assertNotNull(result);
    }

    @Test
    @DisplayName("AAA: abs converts negative to positive")
    void absConvertsNegativeToPositive() {
        // Arrange
        double negative = -42.5;

        // Act
        double result = calculator.abs(negative);

        // Assert
        assertTrue(result > 0,  "Absolute value of a negative must be positive.");
        assertEquals(42.5, result);
    }

    @Test
    @DisplayName("AAA: maxOrNull returns null for equal inputs")
    void maxOrNullReturnsNullForEqualInputs() {
        // Arrange
        double x = 99.0;
        double y = 99.0;

        // Act
        Double result = calculator.maxOrNull(x, y);

        // Assert
        assertNull(result, "Equal inputs must produce a null result from maxOrNull.");
    }

    // ------------------------------------------------------------------
    // Negative AAA Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("AAA (negative): divide by zero throws ArithmeticException")
    void divideByZeroThrowsArithmeticException() {
        // Arrange
        double dividend = 50.0;
        double divisor  = 0.0;

        // Act + Assert (combined for exception testing — this is the JUnit 5 convention)
        ArithmeticException thrown = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(dividend, divisor),
                "Dividing by zero must throw ArithmeticException."
        );

        // Assert on the exception detail
        assertEquals("Division by zero is undefined.", thrown.getMessage());
    }

    @Test
    @DisplayName("AAA (negative): isEven with non-integer throws IllegalArgumentException")
    void isEvenWithDecimalThrowsIllegalArgumentException() {
        // Arrange
        double nonInteger = 5.7;

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.isEven(nonInteger),
                "isEven() must reject non-integer input."
        );
    }

    // ------------------------------------------------------------------
    // Boundary AAA Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("AAA (boundary): add with Double.MAX_VALUE does not throw")
    void addWithMaxValueDoesNotThrow() {
        // Arrange
        double maxVal = Double.MAX_VALUE;

        // Act
        double result = calculator.add(maxVal, 0.0);

        // Assert
        assertDoesNotThrow(() -> calculator.add(maxVal, 0.0));
        assertEquals(maxVal, result);
    }

    @Test
    @DisplayName("AAA (boundary): multiply by zero always yields zero")
    void multiplyByZeroAlwaysYieldsZero() {
        // Arrange
        double largeNumber = 987_654_321.0;
        double zero        = 0.0;

        // Act
        double result = calculator.multiply(largeNumber, zero);

        // Assert
        assertEquals(0.0, result, "Any number × 0 must equal 0.");
    }
}
