package com.cognizant.forecast;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for the {@link Forecast} engine.
 *
 * <p>Uses a 1e-9 delta for all {@code double} comparisons to accommodate
 * floating-point representation differences between strategies.
 */
@DisplayName("Forecast Tests")
class ForecastTest {

    private static final double DELTA = 1e-6;
    private Forecast forecast;

    @BeforeEach
    void setUp() {
        forecast = new Forecast();
    }

    // ------------------------------------------------------------------
    // Positive Tests — core formula
    // ------------------------------------------------------------------

    @Test
    @DisplayName("All strategies produce the same result for standard input")
    void allStrategiesAgreeOnStandardInput() {
        double recursive = forecast.recursiveFutureValue(10_000, 0.08, 10);
        double memoized  = forecast.memoizedFutureValue( 10_000, 0.08, 10);
        double iterative = forecast.iterativeFutureValue(10_000, 0.08, 10);

        // All three must agree within floating-point tolerance
        assertEquals(recursive, memoized,  DELTA);
        assertEquals(recursive, iterative, DELTA);
    }

    @Test
    @DisplayName("0 years must return the present value unchanged")
    void zeroYearsReturnsUnchangedPresentValue() {
        double result = forecast.iterativeFutureValue(5_000, 0.10, 0);
        assertEquals(5_000.0, result, DELTA);
    }

    @Test
    @DisplayName("0% growth rate must return the present value unchanged")
    void zeroGrowthRateReturnsUnchangedPresentValue() {
        double result = forecast.iterativeFutureValue(7_500, 0.0, 15);
        assertEquals(7_500.0, result, DELTA);
    }

    @Test
    @DisplayName("Negative growth rate must reduce the future value below present value")
    void negativeGrowthRateReducesFutureValue() {
        double result = forecast.iterativeFutureValue(10_000, -0.05, 5);
        assertTrue(result < 10_000, "Negative growth must reduce future value.");
    }

    @Test
    @DisplayName("BigDecimal precision variant returns correct 2 d.p. result")
    void bigDecimalVariantProducesCorrectResult() {
        BigDecimal result = forecast.precisionFutureValue(
                BigDecimal.valueOf(1_000),
                BigDecimal.valueOf(0.10),
                1);
        // £1,000 × 1.10 = £1,100.00
        assertEquals(new BigDecimal("1100.00"), result);
    }

    // ------------------------------------------------------------------
    // Negative / Edge Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Recursive: negative years must throw IllegalArgumentException")
    void recursiveThrowsForNegativeYears() {
        assertThrows(IllegalArgumentException.class,
                () -> forecast.recursiveFutureValue(1_000, 0.05, -1));
    }

    @Test
    @DisplayName("Iterative: negative years must throw IllegalArgumentException")
    void iterativeThrowsForNegativeYears() {
        assertThrows(IllegalArgumentException.class,
                () -> forecast.iterativeFutureValue(1_000, 0.05, -1));
    }

    @Test
    @DisplayName("Iterative: growth rate below -100% must throw IllegalArgumentException")
    void iterativeThrowsForInvalidGrowthRate() {
        assertThrows(IllegalArgumentException.class,
                () -> forecast.iterativeFutureValue(1_000, -1.5, 5));
    }

    @Test
    @DisplayName("Iterative: NaN present value must throw IllegalArgumentException")
    void iterativeThrowsForNaNPresentValue() {
        assertThrows(IllegalArgumentException.class,
                () -> forecast.iterativeFutureValue(Double.NaN, 0.05, 5));
    }

    @Test
    @DisplayName("BigDecimal: null present value must throw IllegalArgumentException")
    void bigDecimalThrowsForNullInput() {
        assertThrows(IllegalArgumentException.class,
                () -> forecast.precisionFutureValue(null, BigDecimal.valueOf(0.05), 5));
    }

    // ------------------------------------------------------------------
    // Boundary Tests
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Large year count (100 years) must complete without error")
    void largeYearCountCompletesSuccessfully() {
        assertDoesNotThrow(() -> forecast.iterativeFutureValue(1_000, 0.05, 100));
    }

    @Test
    @DisplayName("Very small growth rate must produce a result marginally above PV")
    void verySmallGrowthRateProducesSmallIncrement() {
        double result = forecast.iterativeFutureValue(1_000, 0.0001, 1);
        assertEquals(1000.10, result, 0.01);
    }
}
