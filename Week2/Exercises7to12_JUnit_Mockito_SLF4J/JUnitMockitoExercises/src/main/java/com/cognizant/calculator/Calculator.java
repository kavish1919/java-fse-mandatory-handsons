package com.cognizant.calculator;

/**
 * Simple arithmetic calculator used as the Subject-Under-Test for
 * Exercises 7, 8, and 9.
 *
 * <p>Design: All methods are instance methods to allow dependency injection
 * and standard JUnit lifecycle management. Pure functions — no side effects.
 */
public class Calculator {

    /**
     * Adds two doubles.
     *
     * @param a first operand
     * @param b second operand
     * @return {@code a + b}
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts {@code b} from {@code a}.
     *
     * @return {@code a - b}
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two doubles.
     *
     * @return {@code a * b}
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides {@code a} by {@code b}.
     *
     * @param a dividend
     * @param b divisor — must not be zero
     * @return {@code a / b}
     * @throws ArithmeticException if {@code b} is zero
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is undefined.");
        }
        return a / b;
    }

    /**
     * Returns the absolute value of {@code n}.
     *
     * @return {@code |n|}
     */
    public double abs(double n) {
        return Math.abs(n);
    }

    /**
     * Returns {@code true} if {@code n} is even.
     *
     * @throws IllegalArgumentException if {@code n} is not an integer value
     */
    public boolean isEven(double n) {
        if (n != Math.floor(n)) {
            throw new IllegalArgumentException(
                    "isEven() requires an integer value; got: " + n);
        }
        return ((long) n % 2) == 0;
    }

    /**
     * Returns the maximum of two numbers, or {@code null} if both are equal.
     *
     * <p>Demonstrates {@code assertNull} / {@code assertNotNull} usage.
     *
     * @return the larger of {@code a} and {@code b}, or {@code null} if equal
     */
    public Double maxOrNull(double a, double b) {
        if (a == b) return null;
        return Math.max(a, b);
    }
}
