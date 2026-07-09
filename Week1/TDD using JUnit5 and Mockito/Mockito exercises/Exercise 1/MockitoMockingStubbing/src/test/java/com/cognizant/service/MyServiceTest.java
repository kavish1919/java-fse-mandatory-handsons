package com.cognizant.service;

import com.cognizant.api.ExternalApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * MyServiceTest – Mockito Exercise 1: Mocking and Stubbing.
 *
 * <p>This class is split into two sections:
 * <ol>
 *   <li><b>PART 1 – Exact solution code</b> from the exercise spec:
 *       {@link #testExternalApi()} — uses {@code Mockito.mock()} programmatically.</li>
 *   <li><b>PART 2 – Extended tests</b> using {@code @ExtendWith(MockitoExtension.class)}
 *       and Mockito annotations ({@code @Mock}, {@code @InjectMocks}) for
 *       cleaner, annotation-driven mock lifecycle management.</li>
 * </ol>
 *
 * <h2>Key Mockito Concepts Demonstrated</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Concept</th><th>API</th><th>Purpose</th></tr>
 *   <tr><td>Create mock</td>
 *       <td>{@code Mockito.mock(Class)} / {@code @Mock}</td>
 *       <td>Creates a proxy that records and stubs method calls</td></tr>
 *   <tr><td>Stubbing</td>
 *       <td>{@code when(mock.method()).thenReturn(value)}</td>
 *       <td>Defines what value the mock returns for a given call</td></tr>
 *   <tr><td>Argument matcher</td>
 *       <td>{@code when(mock.method(anyInt())).thenReturn(value)}</td>
 *       <td>Stubs based on argument pattern instead of exact value</td></tr>
 *   <tr><td>Throw exception</td>
 *       <td>{@code when(mock.method()).thenThrow(RuntimeException.class)}</td>
 *       <td>Simulates failure scenarios</td></tr>
 *   <tr><td>Verify call</td>
 *       <td>{@code verify(mock, times(n)).method(...)}</td>
 *       <td>Asserts that the mock was called exactly n times</td></tr>
 *   <tr><td>Verify no call</td>
 *       <td>{@code verify(mock, never()).method(...)}</td>
 *       <td>Asserts that the mock was never called</td></tr>
 * </table>
 */
@ExtendWith(MockitoExtension.class)   // enables @Mock and @InjectMocks in PART 2
class MyServiceTest {

    // =========================================================================
    // PART 1 — Exact solution code from the exercise specification
    //           Uses Mockito.mock() programmatically (no annotations)
    // =========================================================================

    /**
     * Reproduces the exact solution from the exercise verbatim.
     *
     * <p>Steps:
     * <ol>
     *   <li>Create a mock of {@link ExternalApi} using {@code Mockito.mock()}.</li>
     *   <li>Stub {@code getData()} to return {@code "Mock Data"}.</li>
     *   <li>Inject mock into {@link MyService} via constructor.</li>
     *   <li>Call {@code service.fetchData()} and assert the stubbed value.</li>
     * </ol>
     */
    @Test
    @DisplayName("SPEC: Programmatic mock + stub getData() → 'Mock Data'")
    public void testExternalApi() {
        // Step 1 – Create a mock object for the external API
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        // Step 2 – Stub the method to return a predefined value
        when(mockApi.getData()).thenReturn("Mock Data");

        // Step 3 – Wire mock into the service under test
        MyService service = new MyService(mockApi);

        // Step 4 – Exercise and assert
        String result = service.fetchData();
        assertEquals("Mock Data", result);
    }

    // =========================================================================
    // PART 2 — Extended tests using @Mock / @InjectMocks annotations
    //           MockitoExtension manages the mock lifecycle automatically
    // =========================================================================

    /** Annotation-driven mock — created and reset by MockitoExtension. */
    @Mock
    private ExternalApi mockApi;

    /**
     * MockitoExtension automatically injects {@code mockApi} into
     * {@code MyService} via constructor injection.
     */
    @InjectMocks
    private MyService service;

    // -------------------------------------------------------------------------
    // fetchData() stubbing
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-01: fetchData() returns the value stubbed on getData()")
    void fetchData_ReturnsStubbedValue() {
        // ARRANGE – stub
        when(mockApi.getData()).thenReturn("Stubbed Response");

        // ACT
        String result = service.fetchData();

        // ASSERT
        assertEquals("Stubbed Response", result);
    }

    @Test
    @DisplayName("TC-02: fetchData() propagates null returned by the mock")
    void fetchData_MockReturnsNull_ServiceReturnsNull() {
        // ARRANGE
        when(mockApi.getData()).thenReturn(null);

        // ACT
        String result = service.fetchData();

        // ASSERT
        assertEquals(null, result,
                "fetchData() must return whatever getData() returns, including null");
    }

    // -------------------------------------------------------------------------
    // getServiceStatus() – stubbing with thenReturn on getStatus()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-03: getServiceStatus() maps 'UP' to 'Service is running normally.'")
    void getServiceStatus_WhenUp_ReturnsNormalMessage() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("UP");

        // ACT
        String status = service.getServiceStatus();

        // ASSERT
        assertEquals("Service is running normally.", status);
    }

    @Test
    @DisplayName("TC-04: getServiceStatus() maps 'DOWN' to 'Service is currently unavailable.'")
    void getServiceStatus_WhenDown_ReturnsUnavailableMessage() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("DOWN");

        // ACT
        String status = service.getServiceStatus();

        // ASSERT
        assertEquals("Service is currently unavailable.", status);
    }

    @Test
    @DisplayName("TC-05: getServiceStatus() maps 'DEGRADED' to 'Service is experiencing issues.'")
    void getServiceStatus_WhenDegraded_ReturnsDegradedMessage() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("DEGRADED");

        // ACT
        String status = service.getServiceStatus();

        // ASSERT
        assertEquals("Service is experiencing issues.", status);
    }

    @Test
    @DisplayName("TC-06: getServiceStatus() maps unknown status to 'Service status unknown.'")
    void getServiceStatus_UnknownStatus_ReturnsUnknownMessage() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("MAINTENANCE");

        // ACT
        String status = service.getServiceStatus();

        // ASSERT
        assertEquals("Service status unknown.", status);
    }

    // -------------------------------------------------------------------------
    // greetUser() – argument-based stubbing
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-07: greetUser() returns formatted greeting when user found")
    void greetUser_UserExists_ReturnsGreeting() {
        // ARRANGE – stub for a specific argument value
        when(mockApi.getUserById(42)).thenReturn("Alice Sharma");

        // ACT
        String greeting = service.greetUser(42);

        // ASSERT
        assertEquals("Hello, Alice Sharma!", greeting);
    }

    @Test
    @DisplayName("TC-08: greetUser() returns 'User not found.' when API returns null")
    void greetUser_UserNotFound_ReturnsNotFoundMessage() {
        // ARRANGE – stub returns null for unknown ID
        when(mockApi.getUserById(999)).thenReturn(null);

        // ACT
        String greeting = service.greetUser(999);

        // ASSERT
        assertEquals("User not found.", greeting);
    }

    // -------------------------------------------------------------------------
    // canProcess() – boolean stubbing
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-09: canProcess() returns true when API reports available")
    void canProcess_ApiAvailable_ReturnsTrue() {
        // ARRANGE
        when(mockApi.isAvailable()).thenReturn(true);

        // ACT & ASSERT
        assertTrue(service.canProcess(),
                "canProcess() must return true when isAvailable() is stubbed true");
    }

    @Test
    @DisplayName("TC-10: canProcess() returns false when API reports unavailable")
    void canProcess_ApiUnavailable_ReturnsFalse() {
        // ARRANGE
        when(mockApi.isAvailable()).thenReturn(false);

        // ACT & ASSERT
        assertFalse(service.canProcess(),
                "canProcess() must return false when isAvailable() is stubbed false");
    }

    // -------------------------------------------------------------------------
    // thenThrow() – simulating exceptions from the external API
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-11: fetchData() propagates RuntimeException thrown by the API")
    void fetchData_ApiThrowsException_ExceptionPropagates() {
        // ARRANGE – stub the mock to throw
        when(mockApi.getData()).thenThrow(new RuntimeException("API timeout"));

        // ACT & ASSERT
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.fetchData());
        assertEquals("API timeout", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // verify() – interaction verification
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-12: fetchData() calls getData() exactly once")
    void fetchData_CallsGetDataExactlyOnce() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchData();

        // ASSERT – verify interaction
        verify(mockApi, times(1)).getData();
    }

    @Test
    @DisplayName("TC-13: greetUser() never calls getData() — calls only getUserById()")
    void greetUser_NeverCallsGetData() {
        // ARRANGE
        when(mockApi.getUserById(1)).thenReturn("Bob");

        // ACT
        service.greetUser(1);

        // ASSERT
        verify(mockApi, times(1)).getUserById(1);   // getUserById was called once
        verify(mockApi, never()).getData();          // getData was never called
    }
}
