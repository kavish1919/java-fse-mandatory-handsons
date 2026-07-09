package com.cognizant.service;

import com.cognizant.api.ExternalApi;

/**
 * MyService – the production class under test.
 *
 * <p>Depends on {@link ExternalApi} injected via constructor
 * (Constructor Injection enables straightforward mocking in tests).
 *
 * <p>All methods delegate to {@link ExternalApi} and apply lightweight
 * business logic on top, giving the tests meaningful behaviour to verify.
 */
public class MyService {

    private final ExternalApi externalApi;

    /**
     * Constructs a {@code MyService} with the given {@link ExternalApi}.
     *
     * @param externalApi the API dependency (must not be null)
     * @throws IllegalArgumentException if {@code externalApi} is null
     */
    public MyService(ExternalApi externalApi) {
        if (externalApi == null) {
            throw new IllegalArgumentException("ExternalApi dependency must not be null.");
        }
        this.externalApi = externalApi;
    }

    /**
     * Fetches raw data from the external API.
     *
     * @return the data string returned by the API
     */
    public String fetchData() {
        return externalApi.getData();
    }

    /**
     * Returns a user-facing status message derived from the API health status.
     *
     * <ul>
     *   <li>{@code "UP"}       → "Service is running normally."</li>
     *   <li>{@code "DEGRADED"} → "Service is experiencing issues."</li>
     *   <li>{@code "DOWN"}     → "Service is currently unavailable."</li>
     *   <li>other              → "Service status unknown."</li>
     * </ul>
     *
     * @return a human-readable status description
     */
    public String getServiceStatus() {
        String raw = externalApi.getStatus();
        return switch (raw) {
            case "UP"       -> "Service is running normally.";
            case "DEGRADED" -> "Service is experiencing issues.";
            case "DOWN"     -> "Service is currently unavailable.";
            default         -> "Service status unknown.";
        };
    }

    /**
     * Looks up a user and returns a formatted greeting, or a not-found message.
     *
     * @param userId the user identifier to look up
     * @return greeting string or {@code "User not found."} if the API returns null
     */
    public String greetUser(int userId) {
        String user = externalApi.getUserById(userId);
        if (user == null) {
            return "User not found.";
        }
        return "Hello, " + user + "!";
    }

    /**
     * Returns whether the service can currently process requests.
     *
     * @return {@code true} only if the underlying API reports availability
     */
    public boolean canProcess() {
        return externalApi.isAvailable();
    }
}
