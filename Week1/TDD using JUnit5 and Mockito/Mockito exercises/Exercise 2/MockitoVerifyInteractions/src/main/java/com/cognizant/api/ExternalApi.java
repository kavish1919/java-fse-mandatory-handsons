package com.cognizant.api;

/**
 * ExternalApi – interface representing the third-party API dependency.
 * Mocked in tests so no real network calls are made.
 */
public interface ExternalApi {

    /** Fetches a primary data payload from the external service. */
    String getData();

    /** Returns the current operational status: "UP", "DOWN", or "DEGRADED". */
    String getStatus();

    /**
     * Fetches a user record by ID.
     *
     * @param userId positive user identifier
     * @return user descriptor string, or {@code null} if not found
     */
    String getUserById(int userId);

    /**
     * Posts data to the external service.
     *
     * @param payload the string payload to send (must not be null)
     */
    void postData(String payload);

    /** Returns {@code true} when the API endpoint is reachable. */
    boolean isAvailable();
}
