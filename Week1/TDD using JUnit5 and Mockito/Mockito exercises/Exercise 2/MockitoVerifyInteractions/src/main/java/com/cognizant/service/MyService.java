package com.cognizant.service;

import com.cognizant.api.ExternalApi;

/**
 * MyService – production class under test.
 *
 * <p>Each method delegates to {@link ExternalApi} so that tests can verify
 * <em>which</em> API methods were called, <em>how many times</em>, and
 * <em>with what arguments</em>.
 */
public class MyService {

    private final ExternalApi externalApi;

    public MyService(ExternalApi externalApi) {
        if (externalApi == null) {
            throw new IllegalArgumentException("ExternalApi must not be null.");
        }
        this.externalApi = externalApi;
    }

    /** Calls {@code getData()} exactly once and returns the result. */
    public String fetchData() {
        return externalApi.getData();
    }

    /**
     * Fetches data twice and concatenates the results.
     * Used to verify that {@code getData()} is called exactly 2 times.
     */
    public String fetchDataTwice() {
        return externalApi.getData() + " | " + externalApi.getData();
    }

    /**
     * Fetches the status from the API.
     * Does NOT call {@code getData()} — useful for verifying zero interactions.
     */
    public String fetchStatus() {
        return externalApi.getStatus();
    }

    /**
     * Looks up a user by ID and returns a greeting.
     *
     * @param userId the user identifier
     * @return greeting string, or "User not found." if null
     */
    public String greetUser(int userId) {
        String user = externalApi.getUserById(userId);
        return (user != null) ? "Hello, " + user + "!" : "User not found.";
    }

    /**
     * Validates and sends a payload via the API.
     * Calls {@code postData()} only when payload is non-null and non-blank;
     * otherwise silently ignores the call.
     *
     * @param payload the data to send
     */
    public void sendData(String payload) {
        if (payload != null && !payload.isBlank()) {
            externalApi.postData(payload);
        }
    }

    /**
     * Sends data only when the API is available.
     * Calls {@code isAvailable()} first; if false, {@code postData()} is skipped.
     *
     * @param payload the data to send
     */
    public void sendDataIfAvailable(String payload) {
        if (externalApi.isAvailable()) {
            externalApi.postData(payload);
        }
    }
}
