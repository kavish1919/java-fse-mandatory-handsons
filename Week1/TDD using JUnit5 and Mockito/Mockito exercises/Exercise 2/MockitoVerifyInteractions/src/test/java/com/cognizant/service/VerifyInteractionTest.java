package com.cognizant.service;

import com.cognizant.api.ExternalApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

/**
 * VerifyInteractionTest – Mockito Exercise 2: Verifying Interactions.
 *
 * <p>Organised in two sections:
 * <ol>
 *   <li><b>PART 1</b> – Exact solution code from the exercise specification.</li>
 *   <li><b>PART 2</b> – Extended tests covering every form of Mockito verification.</li>
 * </ol>
 *
 * <h2>Mockito Verification API Quick Reference</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Verification Mode</th><th>Meaning</th></tr>
 *   <tr><td>{@code verify(mock).method()}</td>
 *       <td>Called exactly 1 time (default)</td></tr>
 *   <tr><td>{@code verify(mock, times(n)).method()}</td>
 *       <td>Called exactly n times</td></tr>
 *   <tr><td>{@code verify(mock, never()).method()}</td>
 *       <td>Never called</td></tr>
 *   <tr><td>{@code verify(mock, atLeastOnce()).method()}</td>
 *       <td>Called ≥ 1 time</td></tr>
 *   <tr><td>{@code verify(mock, atLeast(n)).method()}</td>
 *       <td>Called ≥ n times</td></tr>
 *   <tr><td>{@code verify(mock, atMost(n)).method()}</td>
 *       <td>Called ≤ n times</td></tr>
 *   <tr><td>{@code verifyNoInteractions(mock)}</td>
 *       <td>No method was called at all</td></tr>
 *   <tr><td>{@code verifyNoMoreInteractions(mock)}</td>
 *       <td>No un-verified calls remain</td></tr>
 * </table>
 */
@ExtendWith(MockitoExtension.class)
class VerifyInteractionTest {

    // =========================================================================
    // PART 1 — Exact solution code from the exercise specification
    //           Uses Mockito.mock() programmatically (no annotations)
    // =========================================================================

    /**
     * Reproduces the exact solution verbatim:
     * 1. Create mock → 2. Call service method → 3. Verify interaction.
     */
    @Test
    @DisplayName("SPEC: Programmatic mock – verify getData() was called")
    public void testVerifyInteraction() {
        // Step 1 – Create a mock object
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        // Step 2 – Wire the mock into the service and call the method
        MyService service = new MyService(mockApi);
        service.fetchData();

        // Step 3 – Verify the interaction
        verify(mockApi).getData();   // default: exactly once
    }

    // =========================================================================
    // PART 2 — Extended verification tests using @Mock / @InjectMocks
    // =========================================================================

    @Mock
    private ExternalApi mockApi;

    @InjectMocks
    private MyService service;

    // -------------------------------------------------------------------------
    // verify(mock).method()  — default: exactly once
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-01: fetchData() calls getData() exactly once (default verify)")
    void verify_fetchData_CallsGetDataOnce() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchData();

        // ASSERT – verify called once (default, equivalent to times(1))
        verify(mockApi).getData();
    }

    // -------------------------------------------------------------------------
    // verify(mock, times(n))  — exact call count
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-02: fetchDataTwice() calls getData() exactly 2 times")
    void verify_fetchDataTwice_CallsGetDataTwoTimes() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchDataTwice();

        // ASSERT
        verify(mockApi, times(2)).getData();
    }

    @Test
    @DisplayName("TC-03: fetchStatus() calls getStatus() exactly once")
    void verify_fetchStatus_CallsGetStatusOnce() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("UP");

        // ACT
        service.fetchStatus();

        // ASSERT
        verify(mockApi, times(1)).getStatus();
    }

    // -------------------------------------------------------------------------
    // verify(mock, never())  — method was never invoked
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-04: fetchStatus() never calls getData()")
    void verify_fetchStatus_NeverCallsGetData() {
        // ARRANGE
        when(mockApi.getStatus()).thenReturn("UP");

        // ACT
        service.fetchStatus();

        // ASSERT – getStatus was called; getData was not
        verify(mockApi, times(1)).getStatus();
        verify(mockApi, never()).getData();
    }

    @Test
    @DisplayName("TC-05: sendData() with blank payload never calls postData()")
    void verify_sendData_BlankPayload_NeverCallsPostData() {
        // ARRANGE – no stub needed; postData is void and should not be called

        // ACT
        service.sendData("   ");   // blank → service skips the API call

        // ASSERT
        verify(mockApi, never()).postData(anyString());
    }

    // -------------------------------------------------------------------------
    // verify(mock, atLeastOnce()) / atLeast(n) / atMost(n)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-06: fetchDataTwice() calls getData() at least once")
    void verify_fetchDataTwice_CallsGetDataAtLeastOnce() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchDataTwice();

        // ASSERT
        verify(mockApi, atLeastOnce()).getData();
    }

    @Test
    @DisplayName("TC-07: fetchDataTwice() calls getData() at least 2 times")
    void verify_fetchDataTwice_CallsGetDataAtLeastTwoTimes() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchDataTwice();

        // ASSERT
        verify(mockApi, atLeast(2)).getData();
    }

    @Test
    @DisplayName("TC-08: fetchData() calls getData() at most 1 time")
    void verify_fetchData_CallsGetDataAtMostOnce() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchData();

        // ASSERT
        verify(mockApi, atMost(1)).getData();
    }

    // -------------------------------------------------------------------------
    // Argument verification — exact value and argument matchers
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-09: greetUser(42) calls getUserById() with exact argument 42")
    void verify_greetUser_CallsGetUserByIdWithExactArgument() {
        // ARRANGE
        when(mockApi.getUserById(42)).thenReturn("Alice");

        // ACT
        service.greetUser(42);

        // ASSERT – verify the exact argument was passed
        verify(mockApi).getUserById(eq(42));
    }

    @Test
    @DisplayName("TC-10: greetUser() calls getUserById() with any int argument")
    void verify_greetUser_CallsGetUserByIdWithAnyInt() {
        // ARRANGE
        when(mockApi.getUserById(anyInt())).thenReturn("Bob");

        // ACT
        service.greetUser(7);

        // ASSERT – verify using anyInt() matcher
        verify(mockApi).getUserById(anyInt());
    }

    @Test
    @DisplayName("TC-11: sendData('order-001') calls postData() with exact string argument")
    void verify_sendData_CallsPostDataWithExactString() {
        // ARRANGE – postData() is void; no stub required

        // ACT
        service.sendData("order-001");

        // ASSERT – exact argument verification
        verify(mockApi).postData(eq("order-001"));
    }

    @Test
    @DisplayName("TC-12: sendData() with any non-blank payload calls postData() with anyString()")
    void verify_sendData_CallsPostDataWithAnyString() {
        // ARRANGE

        // ACT
        service.sendData("payload-xyz");

        // ASSERT – argument matcher
        verify(mockApi).postData(anyString());
    }

    // -------------------------------------------------------------------------
    // verifyNoInteractions() — no methods called on the mock at all
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-13: verifyNoInteractions — mockApi untouched when service not called")
    void verify_NoInteractions_WhenServiceMethodNotInvoked() {
        // ARRANGE – service is wired but no method is called

        // ACT – intentionally do nothing

        // ASSERT
        verifyNoInteractions(mockApi);
    }

    // -------------------------------------------------------------------------
    // verifyNoMoreInteractions() — no unexpected calls beyond verified ones
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-14: verifyNoMoreInteractions — fetchData() only calls getData(), nothing else")
    void verify_NoMoreInteractions_AfterFetchData() {
        // ARRANGE
        when(mockApi.getData()).thenReturn("data");

        // ACT
        service.fetchData();

        // ASSERT – verify the one expected call, then confirm nothing else happened
        verify(mockApi).getData();
        verifyNoMoreInteractions(mockApi);
    }

    // -------------------------------------------------------------------------
    // Conditional interaction — API not called when condition fails
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("TC-15: sendDataIfAvailable() calls postData() when isAvailable() is true")
    void verify_sendDataIfAvailable_CallsPostDataWhenAvailable() {
        // ARRANGE
        when(mockApi.isAvailable()).thenReturn(true);

        // ACT
        service.sendDataIfAvailable("report-data");

        // ASSERT – both isAvailable and postData were called
        verify(mockApi).isAvailable();
        verify(mockApi).postData("report-data");
    }

    @Test
    @DisplayName("TC-16: sendDataIfAvailable() never calls postData() when isAvailable() is false")
    void verify_sendDataIfAvailable_NeverCallsPostDataWhenUnavailable() {
        // ARRANGE
        when(mockApi.isAvailable()).thenReturn(false);

        // ACT
        service.sendDataIfAvailable("report-data");

        // ASSERT
        verify(mockApi).isAvailable();                    // was checked
        verify(mockApi, never()).postData(anyString());   // was NOT called
    }
}
