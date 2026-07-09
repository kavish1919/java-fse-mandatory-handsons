package com.cognizant.singleton;

/**
 * SingletonDemo – entry-point that demonstrates the Singleton Logger in action.
 *
 * <p>Calls {@link Logger#getInstance()} from multiple simulated "components" and
 * confirms that every reference is identical (same object identity).
 */
public class SingletonDemo {

    public static void main(String[] args) {

        System.out.println("=== Singleton Pattern Demo: Logger ===\n");

        // Simulated Component A
        Logger loggerFromComponentA = Logger.getInstance();
        loggerFromComponentA.info("Application started – acquired Logger from Component A.");

        // Simulated Component B
        Logger loggerFromComponentB = Logger.getInstance();
        loggerFromComponentB.warn("Low disk space detected – acquired Logger from Component B.");

        // Simulated Component C
        Logger loggerFromComponentC = Logger.getInstance();
        loggerFromComponentC.error("Database connection failed – acquired Logger from Component C.");

        // Identity checks
        System.out.println("\n--- Instance Identity Checks ---");
        System.out.printf("Component A identity hash : %s%n",
                Integer.toHexString(System.identityHashCode(loggerFromComponentA)));
        System.out.printf("Component B identity hash : %s%n",
                Integer.toHexString(System.identityHashCode(loggerFromComponentB)));
        System.out.printf("Component C identity hash : %s%n",
                Integer.toHexString(System.identityHashCode(loggerFromComponentC)));

        boolean allSame = (loggerFromComponentA == loggerFromComponentB)
                       && (loggerFromComponentB == loggerFromComponentC);

        System.out.println("\nAll references point to the same instance? " + allSame);
        System.out.println(allSame
                ? "✔ Singleton pattern is working correctly."
                : "✘ Singleton pattern is BROKEN – multiple instances detected!");
    }
}
