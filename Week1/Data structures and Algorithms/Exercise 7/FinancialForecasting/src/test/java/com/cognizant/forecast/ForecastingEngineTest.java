package com.cognizant.forecast;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ForecastingEngineTest – JUnit 5 test suite verifying all four implementations.
 */
class ForecastingEngineTest {

    private static final double DELTA = 0.001; // acceptable floating-point tolerance

    private ForecastingEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ForecastingEngine();
    }

    // =========================================================================
    // futureValueRecursive — constant rate
    // =========================================================================

    @Test
    @DisplayName("TC-01 [Recursive] 0 periods → returns presentValue unchanged")
    void recursive_zeroPeriods() {
        assertEquals(5000.0, engine.futureValueRecursive(5000.0, 0.10, 0), DELTA);
    }

    @Test
    @DisplayName("TC-02 [Recursive] 0% growth rate → value unchanged across periods")
    void recursive_zeroGrowthRate() {
        assertEquals(1000.0, engine.futureValueRecursive(1000.0, 0.0, 5), DELTA);
    }

    @Test
    @DisplayName("TC-03 [Recursive] 1 period — single compounding step")
    void recursive_onePeriod() {
        // FV = 1000 × 1.08 = 1080
        assertEquals(1080.0, engine.futureValueRecursive(1000.0, 0.08, 1), DELTA);
    }

    @Test
    @DisplayName("TC-04 [Recursive] 3 periods — matches manual calculation 1000×1.10^3=1331")
    void recursive_threePeriods() {
        assertEquals(1331.0, engine.futureValueRecursive(1000.0, 0.10, 3), DELTA);
    }

    @Test
    @DisplayName("TC-05 [Recursive] 10 periods at 8% — matches formula PV×(1+r)^n")
    void recursive_tenPeriods() {
        double expected = 10000.0 * Math.pow(1.08, 10);
        assertEquals(expected, engine.futureValueRecursive(10000.0, 0.08, 10), DELTA);
    }

    @Test
    @DisplayName("TC-06 [Recursive] Negative growth rate (loss scenario) — value decreases")
    void recursive_negativeGrowthRate() {
        double result = engine.futureValueRecursive(1000.0, -0.10, 3);
        assertTrue(result < 1000.0, "Value should decrease with negative growth.");
        assertEquals(1000.0 * Math.pow(0.90, 3), result, DELTA);
    }

    @Test
    @DisplayName("TC-07 [Recursive] Invalid periods (<0) → IllegalArgumentException")
    void recursive_invalidPeriods() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.futureValueRecursive(1000.0, 0.05, -1));
    }

    @Test
    @DisplayName("TC-08 [Recursive] Invalid growthRate (≤-1) → IllegalArgumentException")
    void recursive_invalidGrowthRate() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.futureValueRecursive(1000.0, -1.0, 5));
    }

    // =========================================================================
    // futureValueMemo — memoized constant rate
    // =========================================================================

    @Test
    @DisplayName("TC-09 [Memo] Result matches pure recursive for 10 periods at 8%")
    void memo_matchesRecursive() {
        double recursive = engine.futureValueRecursive(10000.0, 0.08, 10);
        double memo      = engine.futureValueMemo(10000.0, 0.08, 10);
        assertEquals(recursive, memo, DELTA);
    }

    @Test
    @DisplayName("TC-10 [Memo] Cache is populated with correct intermediate values")
    void memo_cachePopulated() {
        Map<Integer, Double> cache = new HashMap<>();
        engine.futureValueMemo(1000.0, 0.10, 3, cache);

        // Cache should contain entries for periods 1, 2, 3 (not 0 — that's the base case)
        assertEquals(3, cache.size());
        assertEquals(1100.0, cache.get(1), DELTA);   // 1000 × 1.10
        assertEquals(1210.0, cache.get(2), DELTA);   // 1100 × 1.10
        assertEquals(1331.0, cache.get(3), DELTA);   // 1210 × 1.10
    }

    @Test
    @DisplayName("TC-11 [Memo] 0 periods → returns presentValue")
    void memo_zeroPeriods() {
        assertEquals(7500.0, engine.futureValueMemo(7500.0, 0.05, 0), DELTA);
    }

    // =========================================================================
    // futureValueVariableRates
    // =========================================================================

    @Test
    @DisplayName("TC-12 [Variable] 0 periods → returns presentValue")
    void variable_zeroPeriods() {
        double[] rates = {0.05, 0.08};
        assertEquals(1000.0, engine.futureValueVariableRates(1000.0, rates, 0), DELTA);
    }

    @Test
    @DisplayName("TC-13 [Variable] 2 periods with known rates — manual verification")
    void variable_twoPeriods() {
        // Period 1: 1000 × 1.05 = 1050
        // Period 2: 1050 × 1.08 = 1134
        double[] rates = {0.05, 0.08};
        assertEquals(1134.0, engine.futureValueVariableRates(1000.0, rates, 2), DELTA);
    }

    @Test
    @DisplayName("TC-14 [Variable] Negative rate in one period — value drops then recovers")
    void variable_negativeRateInOnePeriod() {
        // Period 1: 1000 × 1.20 = 1200; Period 2: 1200 × 0.90 = 1080
        double[] rates = {0.20, -0.10};
        assertEquals(1080.0, engine.futureValueVariableRates(1000.0, rates, 2), DELTA);
    }

    @Test
    @DisplayName("TC-15 [Variable] Null growthRates → IllegalArgumentException")
    void variable_nullRates() {
        assertThrows(IllegalArgumentException.class,
                () -> engine.futureValueVariableRates(1000.0, null, 1));
    }

    @Test
    @DisplayName("TC-16 [Variable] periodIndex out of range → IllegalArgumentException")
    void variable_periodIndexOutOfRange() {
        double[] rates = {0.05};
        assertThrows(IllegalArgumentException.class,
                () -> engine.futureValueVariableRates(1000.0, rates, 5));
    }

    // =========================================================================
    // Iterative baseline
    // =========================================================================

    @Test
    @DisplayName("TC-17 [Iterative] 10 periods at 8% — matches formula")
    void iterative_tenPeriods() {
        double expected = 10000.0 * Math.pow(1.08, 10);
        assertEquals(expected, engine.futureValueIterative(10000.0, 0.08, 10), DELTA);
    }

    @Test
    @DisplayName("TC-18 [Iterative] 0 periods → returns presentValue")
    void iterative_zeroPeriods() {
        assertEquals(3000.0, engine.futureValueIterative(3000.0, 0.07, 0), DELTA);
    }

    // =========================================================================
    // Cross-algorithm consistency
    // =========================================================================

    @Test
    @DisplayName("TC-19 [Cross] Recursive, Memoized, and Iterative all agree for n=20")
    void crossAlgorithm_allAgree() {
        double pv   = 50_000.0;
        double rate = 0.06;
        int    n    = 20;

        double rec  = engine.futureValueRecursive(pv, rate, n);
        double memo = engine.futureValueMemo(pv, rate, n);
        double iter = engine.futureValueIterative(pv, rate, n);

        assertEquals(rec, memo, DELTA, "Recursive vs Memoized mismatch.");
        assertEquals(rec, iter, DELTA, "Recursive vs Iterative mismatch.");
    }
}
