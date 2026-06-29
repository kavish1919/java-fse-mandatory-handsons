package com.cognizant.forecast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Financial forecasting engine providing recursive, memoised, and iterative
 * compound-growth calculations.
 *
 * <h2>Formula</h2>
 * <pre>futureValue = presentValue × (1 + growthRate) ^ years</pre>
 *
 * <h2>Complexity Analysis</h2>
 * <table border="1">
 *   <tr><th>Strategy</th><th>Time</th><th>Space</th><th>Notes</th></tr>
 *   <tr><td>Recursive (naïve)</td><td>O(n)</td><td>O(n) stack</td>
 *       <td>Stack-overflow risk for very large n</td></tr>
 *   <tr><td>Recursive + Memo</td><td>O(n)</td><td>O(n) heap</td>
 *       <td>Each sub-problem computed once; trades stack depth for heap</td></tr>
 *   <tr><td>Iterative</td><td>O(n)</td><td>O(1)</td>
 *       <td>Preferred for production; no call-stack risk</td></tr>
 * </table>
 *
 * <h2>Why Recursion Here?</h2>
 * The compound-growth recurrence is:
 * <pre>
 *   FV(0)       = presentValue
 *   FV(years)   = FV(years-1) × (1 + growthRate)
 * </pre>
 * This naturally expresses as recursion, making the mathematical intent clear.
 * However, for large {@code years} values the iterative form is always preferred.
 */
public class Forecast {

    /** Scale for monetary rounding (2 decimal places = cents). */
    private static final int MONEY_SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private void validate(double presentValue, double growthRate, int years) {
        if (years < 0) {
            throw new IllegalArgumentException(
                    "Years must be >= 0; got: " + years);
        }
        if (growthRate < -1.0) {
            throw new IllegalArgumentException(
                    "growthRate cannot be less than -1 (i.e., -100%); got: " + growthRate);
        }
        if (Double.isNaN(presentValue) || Double.isInfinite(presentValue)) {
            throw new IllegalArgumentException(
                    "presentValue must be a finite number; got: " + presentValue);
        }
    }

    // -------------------------------------------------------------------------
    // 1. Naïve Recursive  —  O(n) time, O(n) stack space
    // -------------------------------------------------------------------------

    /**
     * Calculates future value using naïve recursion.
     *
     * <p><strong>Caution:</strong> each recursive call consumes a stack frame.
     * For {@code years} > ~8 000 on the default JVM stack, a
     * {@link StackOverflowError} will occur. Use {@link #iterativeFutureValue}
     * or {@link #memoizedFutureValue} for large inputs.
     *
     * @param presentValue the current principal amount
     * @param growthRate   annual growth rate (e.g. 0.08 for 8 %)
     * @param years        number of years to project (must be >= 0)
     * @return projected future value
     */
    public double recursiveFutureValue(double presentValue, double growthRate, int years) {
        validate(presentValue, growthRate, years);
        return recursiveHelper(presentValue, growthRate, years);
    }

    private double recursiveHelper(double presentValue, double growthRate, int years) {
        if (years == 0) {
            return presentValue;                                 // base case
        }
        return recursiveHelper(presentValue, growthRate, years - 1) * (1 + growthRate);
    }

    // -------------------------------------------------------------------------
    // 2. Memoised Recursive  —  O(n) time, O(n) heap space
    // -------------------------------------------------------------------------

    /**
     * Calculates future value using memoised recursion.
     *
     * <p>Memoisation stores the result of each sub-problem in a {@link Map} so
     * it is computed only once. Although the compound-growth recurrence has no
     * overlapping sub-problems (unlike Fibonacci), the pattern is demonstrated
     * here for educational completeness and extendability.
     *
     * @param presentValue the current principal amount
     * @param growthRate   annual growth rate
     * @param years        number of years to project (must be >= 0)
     * @return projected future value
     */
    public double memoizedFutureValue(double presentValue, double growthRate, int years) {
        validate(presentValue, growthRate, years);
        Map<Integer, Double> memo = new HashMap<>();
        return memoHelper(presentValue, growthRate, years, memo);
    }

    private double memoHelper(double pv, double rate, int years, Map<Integer, Double> memo) {
        if (years == 0) return pv;
        if (memo.containsKey(years)) return memo.get(years);
        double result = memoHelper(pv, rate, years - 1, memo) * (1 + rate);
        memo.put(years, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // 3. Iterative  —  O(n) time, O(1) space  [PREFERRED for production]
    // -------------------------------------------------------------------------

    /**
     * Calculates future value iteratively — the recommended production approach.
     *
     * <p>Equivalent to: {@code presentValue × (1 + growthRate) ^ years},
     * computed step-by-step to avoid floating-point exponentiation drift.
     *
     * @param presentValue the current principal amount
     * @param growthRate   annual growth rate
     * @param years        number of years to project (must be >= 0)
     * @return projected future value
     */
    public double iterativeFutureValue(double presentValue, double growthRate, int years) {
        validate(presentValue, growthRate, years);
        double result = presentValue;
        for (int y = 0; y < years; y++) {
            result *= (1 + growthRate);
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // 4. High-precision  —  uses BigDecimal for financial accuracy
    // -------------------------------------------------------------------------

    /**
     * Calculates future value with full monetary precision using {@link BigDecimal}.
     *
     * <p>Floating-point double is unsuitable for monetary calculations due to
     * binary representation errors (e.g. 0.1 + 0.2 ≠ 0.3 exactly). This method
     * uses {@link BigDecimal} with {@link MathContext#DECIMAL128} precision.
     *
     * @param presentValue the current principal (must be non-negative)
     * @param growthRate   annual growth rate
     * @param years        number of years to project (must be >= 0)
     * @return precisely rounded future value to 2 decimal places
     */
    public BigDecimal precisionFutureValue(BigDecimal presentValue,
                                           BigDecimal growthRate,
                                           int years) {
        if (years < 0) throw new IllegalArgumentException("Years must be >= 0.");
        if (presentValue == null || growthRate == null) {
            throw new IllegalArgumentException("Inputs must not be null.");
        }

        BigDecimal factor = BigDecimal.ONE.add(growthRate);
        BigDecimal result = presentValue;

        for (int y = 0; y < years; y++) {
            result = result.multiply(factor, MathContext.DECIMAL128);
        }

        return result.setScale(MONEY_SCALE, ROUNDING);
    }

    // -------------------------------------------------------------------------
    // Formatting helper
    // -------------------------------------------------------------------------

    /**
     * Rounds a {@code double} to 2 decimal places for display.
     */
    public static double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(MONEY_SCALE, ROUNDING)
                .doubleValue();
    }
}
