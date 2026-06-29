package com.cognizant.forecast;

import java.math.BigDecimal;

/**
 * Demonstration of the financial forecasting tool.
 *
 * <p>Compares recursive, memoised, iterative, and BigDecimal strategies
 * across various scenarios, and reports edge-case handling.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Financial Forecasting Tool — Compound Growth Predictor ===\n");

        Forecast forecast = new Forecast();

        double presentValue = 10_000.00;  // £10,000 initial investment
        double growthRate   = 0.08;       // 8 % annual growth
        int    years        = 10;

        // ---------------------------------------------------------------
        // Core comparison
        // ---------------------------------------------------------------
        System.out.printf("Present Value : £%,.2f%n", presentValue);
        System.out.printf("Growth Rate   : %.0f%%%n", growthRate * 100);
        System.out.printf("Years         : %d%n%n", years);

        double recursive = forecast.recursiveFutureValue(presentValue, growthRate, years);
        double memoized  = forecast.memoizedFutureValue( presentValue, growthRate, years);
        double iterative = forecast.iterativeFutureValue(presentValue, growthRate, years);

        BigDecimal precision = forecast.precisionFutureValue(
                BigDecimal.valueOf(presentValue),
                BigDecimal.valueOf(growthRate),
                years);

        System.out.println("  Strategy          Future Value");
        System.out.println("  ─────────────     ────────────");
        System.out.printf("  Recursive   :  £%,.2f%n", Forecast.round(recursive));
        System.out.printf("  Memoised    :  £%,.2f%n", Forecast.round(memoized));
        System.out.printf("  Iterative   :  £%,.2f%n", Forecast.round(iterative));
        System.out.printf("  BigDecimal  :  £%s%n%n",  precision.toPlainString());

        // ---------------------------------------------------------------
        // Growth table over multiple horizons
        // ---------------------------------------------------------------
        System.out.println("  Year-by-year Growth Table:");
        System.out.printf("  %-6s  %s%n", "Year", "Future Value");
        System.out.println("  ─────────────────────────");
        for (int y = 1; y <= 20; y++) {
            double fv = forecast.iterativeFutureValue(presentValue, growthRate, y);
            System.out.printf("  %-6d  £%,.2f%n", y, Forecast.round(fv));
        }

        // ---------------------------------------------------------------
        // Edge cases
        // ---------------------------------------------------------------
        System.out.println("\n  --- Edge Cases ---");

        System.out.printf("  0 years   : £%,.2f (no growth — returns present value)%n",
                Forecast.round(forecast.iterativeFutureValue(presentValue, growthRate, 0)));

        System.out.printf("  0%% rate   : £%,.2f (no growth — returns present value)%n",
                Forecast.round(forecast.iterativeFutureValue(presentValue, 0.0, years)));

        System.out.printf("  Negative  : £%,.2f (-5%% annual decline over 10 years)%n",
                Forecast.round(forecast.iterativeFutureValue(presentValue, -0.05, years)));

        try {
            forecast.iterativeFutureValue(presentValue, growthRate, -1);
        } catch (IllegalArgumentException ex) {
            System.out.println("  years=-1  : Caught — " + ex.getMessage());
        }

        System.out.println("\n=== Demo Complete ===");
    }
}
