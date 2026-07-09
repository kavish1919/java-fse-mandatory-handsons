package com.cognizant.calculator;

/**
 * Calculator – simple arithmetic utility class used as the Subject Under Test
 * for JUnit Exercise 1.
 *
 * <p>Provides the four basic operations: add, subtract, multiply, and divide.
 * These operations are intentionally straightforward so that the focus of
 * Exercise 1 remains on <em>setting up JUnit</em> rather than on complex
 * business logic.
 */
public class Calculator {

    /**
     * Adds two integers.
     *
     * @param a first operand
     * @param b second operand
     * @return the sum {@code a + b}
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts the second integer from the first.
     *
     * @param a minuend
     * @param b subtrahend
     * @return the difference {@code a - b}
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two integers.
     *
     * @param a first factor
     * @param b second factor
     * @return the product {@code a * b}
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides the first integer by the second.
     *
     * @param a dividend
     * @param b divisor (must not be zero)
     * @return the integer quotient {@code a / b}
     * @throws ArithmeticException if {@code b} is zero
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }
}
