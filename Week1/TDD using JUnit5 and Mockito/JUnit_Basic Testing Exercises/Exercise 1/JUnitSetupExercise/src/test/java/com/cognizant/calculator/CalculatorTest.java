package com.cognizant.calculator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * CalculatorTest – JUnit 4 test class for {@link Calculator}.
 *
 * <p>This class demonstrates the core JUnit 4 setup:
 * <ul>
 *   <li>{@code @Before}  – runs before each test method (fixture setup)</li>
 *   <li>{@code @After}   – runs after each test method (fixture teardown)</li>
 *   <li>{@code @Test}    – marks a method as a test case</li>
 *   <li>{@code @Test(expected = ...)} – verifies an exception is thrown</li>
 *   <li>{@code assertEquals} / {@code fail} – JUnit 4 assertion methods</li>
 * </ul>
 *
 * <p><b>How to run:</b>
 * <pre>
 *   mvn test
 * </pre>
 * or right-click the class in IntelliJ / Eclipse and choose "Run".
 */
public class CalculatorTest {

    // The object under test; created fresh before every test method
    private Calculator calculator;

    // -------------------------------------------------------------------------
    // Fixture lifecycle
    // -------------------------------------------------------------------------

    /**
     * Executed BEFORE each test method.
     * Creates a new Calculator instance to ensure test isolation.
     */
    @Before
    public void setUp() {
        calculator = new Calculator();
        System.out.println("[setUp] Calculator instance created.");
    }

    /**
     * Executed AFTER each test method.
     * Releases resources (here just nulls the reference as good practice).
     */
    @After
    public void tearDown() {
        calculator = null;
        System.out.println("[tearDown] Calculator instance released.");
    }

    // -------------------------------------------------------------------------
    // add()
    // -------------------------------------------------------------------------

    @Test
    public void testAdd_TwoPositiveNumbers() {
        int result = calculator.add(3, 7);
        assertEquals("3 + 7 should equal 10", 10, result);
    }

    @Test
    public void testAdd_PositiveAndNegative() {
        int result = calculator.add(10, -4);
        assertEquals("10 + (-4) should equal 6", 6, result);
    }

    @Test
    public void testAdd_BothNegative() {
        int result = calculator.add(-5, -3);
        assertEquals("-5 + (-3) should equal -8", -8, result);
    }

    @Test
    public void testAdd_WithZero() {
        int result = calculator.add(0, 99);
        assertEquals("0 + 99 should equal 99", 99, result);
    }

    // -------------------------------------------------------------------------
    // subtract()
    // -------------------------------------------------------------------------

    @Test
    public void testSubtract_BasicSubtraction() {
        int result = calculator.subtract(10, 4);
        assertEquals("10 - 4 should equal 6", 6, result);
    }

    @Test
    public void testSubtract_ResultIsNegative() {
        int result = calculator.subtract(3, 9);
        assertEquals("3 - 9 should equal -6", -6, result);
    }

    @Test
    public void testSubtract_SameOperands() {
        int result = calculator.subtract(7, 7);
        assertEquals("7 - 7 should equal 0", 0, result);
    }

    // -------------------------------------------------------------------------
    // multiply()
    // -------------------------------------------------------------------------

    @Test
    public void testMultiply_TwoPositiveNumbers() {
        int result = calculator.multiply(4, 5);
        assertEquals("4 × 5 should equal 20", 20, result);
    }

    @Test
    public void testMultiply_ByZero() {
        int result = calculator.multiply(100, 0);
        assertEquals("100 × 0 should equal 0", 0, result);
    }

    @Test
    public void testMultiply_NegativeNumbers() {
        int result = calculator.multiply(-3, -4);
        assertEquals("-3 × -4 should equal 12", 12, result);
    }

    @Test
    public void testMultiply_PositiveAndNegative() {
        int result = calculator.multiply(6, -2);
        assertEquals("6 × (-2) should equal -12", -12, result);
    }

    // -------------------------------------------------------------------------
    // divide()
    // -------------------------------------------------------------------------

    @Test
    public void testDivide_EvenDivision() {
        int result = calculator.divide(20, 4);
        assertEquals("20 / 4 should equal 5", 5, result);
    }

    @Test
    public void testDivide_IntegerTruncation() {
        int result = calculator.divide(7, 2);
        assertEquals("7 / 2 should equal 3 (integer truncation)", 3, result);
    }

    @Test
    public void testDivide_DividendIsZero() {
        int result = calculator.divide(0, 5);
        assertEquals("0 / 5 should equal 0", 0, result);
    }

    /**
     * JUnit 4 style: use {@code expected} attribute to assert an exception type.
     * The test PASSES only when {@link ArithmeticException} is thrown.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivide_ByZero_ThrowsArithmeticException() {
        calculator.divide(10, 0);
        // If no exception is thrown, JUnit 4 marks this test as FAILED
    }

    /**
     * Alternative style using try/catch + {@code fail()} to verify both the
     * exception type AND the message.
     */
    @Test
    public void testDivide_ByZero_ExceptionMessageContainsDivisionByZero() {
        try {
            calculator.divide(10, 0);
            fail("Expected ArithmeticException was not thrown.");
        } catch (ArithmeticException ex) {
            assertEquals("Division by zero is not allowed.", ex.getMessage());
        }
    }
}
