package com.cognizant.api;

/**
 * ExternalApi – interface representing a third-party API dependency.
 *
 * <p>In production this would be implemented by a class that makes real
 * HTTP/REST calls. In tests we will <em>mock</em> this interface so that
 * no real network calls are made.
 *
 * <p>Methods exposed:
 * <ul>
 *   <li>{@link #getData()}         – fetches a primary data payload</li>
 *   <li>{@link #getStatus()}       – returns the current API health status</li>
 *   <li>{@link #getUserById(int)}  – fetches a user record by ID</li>
 *   <li>{@link #isAvailable()}     – returns true when the API is reachable</li>
 * </ul>
 */
public interface ExternalApi {

    /**
     * Fetches a primary data payload from the external service.
     *
     * @return raw data string; never null in the real implementation
     */
    String getData();

    /**
     * Returns the current operational status of the external API.
     *
     * @return one of: {@code "UP"}, {@code "DOWN"}, {@code "DEGRADED"}
     */
    String getStatus();

    /**
     * Fetches a user record for the given identifier.
     *
     * @param userId unique user identifier (positive integer)
     * @return a user descriptor string, or {@code null} if not found
     */
    String getUserById(int userId);

    /**
     * Checks whether the external API endpoint is currently reachable.
     *
     * @return {@code true} if the API is available; {@code false} otherwise
     */
    boolean isAvailable();
}
