package com.cognizant.forecast;

import java.util.HashMap;
import java.util.Map;

/**
 * ForecastingEngine – predicts future financial values using three approaches:
 *
 * <ol>
 *   <li><b>Recursive (constant rate)</b>   – clean, direct translation of
 *       the mathematical definition; O(n) time, O(n) stack space.</li>
 *   <li><b>Recursive with Memoization</b>  – caches sub-results to eliminate
 *       redundant computation in variable-rate multi-branch scenarios;
 *       O(n) time, O(n) heap space.</li>
 *   <li><b>Recursive (variable rates)</b>  – accepts a different growth rate
 *       per period (realistic historical data); O(n) time, O(n) stack space.</li>
 *   <li><b>Iterative (baseline)</b>         – O(n) time, O(1) space reference
 *       implementation used to verify recursive results.</li>
 * </ol>
 *
 * <h2>Mathematical Foundation</h2>
 * <p>Future Value with a constant growth rate {@code r} over {@code n} periods:
 * <pre>
 *   FV(PV, r, n) = PV × (1 + r)^n
 * </pre>
 *
 * <p>Recursive decomposition (the key insight):
 * <pre>
 *   FV(PV, r, 0) = PV                        ← base case
 *   FV(PV, r, n) = FV(PV, r, n-1) × (1 + r)  ← recursive case
 * </pre>
 *
 * <p>Each call reduces {@code n} by 1 until the base case is reached — exactly
 * how recursion "simplifies" the problem into smaller sub-problems.
 */
public class ForecastingEngine {

    // =========================================================================
    // 1. Pure Recursive — constant growth rate
    // =========================================================================

    /**
     * Calculates future value recursively with a constant annual growth rate.
     *
     * <h3>Recursion trace (PV=1000, r=0.10, n=3):</h3>
     * <pre>
     *   fv(1000, 0.10, 3)
     *     → fv(1000, 0.10, 2) × 1.10
     *         → fv(1000, 0.10, 1) × 1.10
     *             → fv(1000, 0.10, 0) × 1.10
     *                 → 1000              [base case]
     *             = 1000 × 1.10 = 1100
     *         = 1100 × 1.10 = 1210
     *     = 1210 × 1.10 = 1331
     * </pre>
     *
     * <p><b>Time complexity:</b> O(n) — one recursive call per period.<br>
     * <b>Space complexity:</b> O(n) — call-stack depth equals {@code periods}.
     *
     * @param presentValue the initial investment amount (must be ≥ 0)
     * @param growthRate   the periodic growth rate, e.g. 0.08 = 8% (must be > -1)
     * @param periods      number of compounding periods (must be ≥ 0)
     * @return the predicted future value
     * @throws IllegalArgumentException for invalid arguments
     */
    public double futureValueRecursive(double presentValue, double growthRate, int periods) {
        validateInputs(presentValue, growthRate, periods);

        // ── Base case ──────────────────────────────────────────────────────────
        if (periods == 0) {
            return presentValue;
        }

        // ── Recursive case ─────────────────────────────────────────────────────
        // Reduce the problem: n periods → (n-1) periods, then apply one growth step
        return futureValueRecursive(presentValue, growthRate, periods - 1) * (1 + growthRate);
    }

    // =========================================================================
    // 2. Memoized Recursive — constant growth rate
    // =========================================================================

    /**
     * Calculates future value using recursion with memoization (top-down DP).
     *
     * <p>For a constant growth-rate scenario the sub-problems are unique per
     * {@code periods} value, so the cache maps {@code periods → futureValue}.
     * Each unique sub-problem is computed exactly once; repeated look-ups are O(1).
     *
     * <p><b>Time complexity:</b> O(n) — n unique sub-problems, each solved once.<br>
     * <b>Space complexity:</b> O(n) — cache entries + reduced stack depth for
     * already-cached values.
     *
     * <p>Memoization becomes critical in <em>multi-branch</em> recursive
     * formulations (e.g., Monte Carlo trees or decision trees) where the same
     * sub-problem recurs across many branches — turning exponential O(2^n) into
     * polynomial time.
     *
     * @param presentValue the initial investment amount (≥ 0)
     * @param growthRate   the periodic growth rate (> -1)
     * @param periods      number of compounding periods (≥ 0)
     * @param cache        caller-provided memoization map (pass {@code new HashMap<>()})
     * @return the predicted future value
     */
    public double futureValueMemo(double presentValue,
                                   double growthRate,
                                   int periods,
                                   Map<Integer, Double> cache) {
        validateInputs(presentValue, growthRate, periods);

        // ── Base case ──────────────────────────────────────────────────────────
        if (periods == 0) {
            return presentValue;
        }

        // ── Cache hit ──────────────────────────────────────────────────────────
        if (cache.containsKey(periods)) {
            return cache.get(periods);
        }

        // ── Recursive case + store result ──────────────────────────────────────
        double result = futureValueMemo(presentValue, growthRate, periods - 1, cache)
                        * (1 + growthRate);
        cache.put(periods, result);
        return result;
    }

    /**
     * Convenience overload of {@link #futureValueMemo} that allocates the cache
     * internally.
     *
     * @param presentValue the initial investment amount
     * @param growthRate   the periodic growth rate
     * @param periods      number of compounding periods
     * @return the predicted future value
     */
    public double futureValueMemo(double presentValue, double growthRate, int periods) {
        return futureValueMemo(presentValue, growthRate, periods, new HashMap<>());
    }

    // =========================================================================
    // 3. Recursive — variable growth rates per period (realistic scenario)
    // =========================================================================

    /**
     * Calculates future value recursively when the growth rate differs each period.
     *
     * <p>This models real historical data where past returns are not uniform.
     * {@code growthRates[0]} is applied in period 1, {@code growthRates[1]}
     * in period 2, etc.
     *
     * <pre>
     *   FV(PV, rates, 0) = PV                             ← base case (all periods consumed)
     *   FV(PV, rates, i) = FV(PV, rates, i-1) × (1 + rates[i-1])
     * </pre>
     *
     * <p><b>Time complexity:</b> O(n) — one call per period.<br>
     * <b>Space complexity:</b> O(n) — call-stack depth equals {@code periodIndex}.
     *
     * @param presentValue the initial investment amount (≥ 0)
     * @param growthRates  array of per-period growth rates (none may be ≤ -1)
     * @param periodIndex  current period index; pass {@code growthRates.length}
     *                     to forecast the full array
     * @return the predicted future value after {@code periodIndex} periods
     * @throws IllegalArgumentException for invalid arguments
     */
    public double futureValueVariableRates(double presentValue,
                                            double[] growthRates,
                                            int periodIndex) {
        if (presentValue < 0) {
            throw new IllegalArgumentException("presentValue must be ≥ 0.");
        }
        if (growthRates == null || growthRates.length == 0) {
            throw new IllegalArgumentException("growthRates must not be null or empty.");
        }
        if (periodIndex < 0 || periodIndex > growthRates.length) {
            throw new IllegalArgumentException(
                    "periodIndex must be in [0, growthRates.length].");
        }
        for (double r : growthRates) {
            if (r <= -1.0) {
                throw new IllegalArgumentException(
                        "Each growth rate must be > -1 (i.e., loss cannot exceed 100%).");
            }
        }

        // ── Base case ──────────────────────────────────────────────────────────
        if (periodIndex == 0) {
            return presentValue;
        }

        // ── Recursive case ─────────────────────────────────────────────────────
        double previousValue = futureValueVariableRates(presentValue, growthRates, periodIndex - 1);
        return previousValue * (1 + growthRates[periodIndex - 1]);
    }

    // =========================================================================
    // 4. Iterative baseline — O(n) time, O(1) space
    // =========================================================================

    /**
     * Calculates future value iteratively (reference implementation for verification).
     *
     * <p><b>Time complexity:</b> O(n).<br>
     * <b>Space complexity:</b> O(1) — no call stack, no cache.</p>
     *
     * @param presentValue the initial investment amount (≥ 0)
     * @param growthRate   the periodic growth rate (> -1)
     * @param periods      number of compounding periods (≥ 0)
     * @return the predicted future value
     */
    public double futureValueIterative(double presentValue, double growthRate, int periods) {
        validateInputs(presentValue, growthRate, periods);
        double value = presentValue;
        for (int i = 0; i < periods; i++) {
            value *= (1 + growthRate);
        }
        return value;
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private void validateInputs(double presentValue, double growthRate, int periods) {
        if (presentValue < 0) {
            throw new IllegalArgumentException("presentValue must be ≥ 0.");
        }
        if (growthRate <= -1.0) {
            throw new IllegalArgumentException("growthRate must be > -1.");
        }
        if (periods < 0) {
            throw new IllegalArgumentException("periods must be ≥ 0.");
        }
    }
}
