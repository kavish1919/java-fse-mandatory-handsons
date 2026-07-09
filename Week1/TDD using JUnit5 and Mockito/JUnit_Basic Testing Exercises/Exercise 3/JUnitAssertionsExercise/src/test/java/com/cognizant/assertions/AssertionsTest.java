package com.cognizant.assertions;

import com.cognizant.bank.BankAccount;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * AssertionsTest – Exercise 3: Assertions in JUnit 4.
 *
 * <p>This class demonstrates every major JUnit 4 assertion method, organised
 * into two sections:
 *
 * <ol>
 *   <li><b>Exact solution from the exercise specification</b> ({@link #testAssertions()})
 *       – reproduced verbatim as required.</li>
 *   <li><b>Extended assertions</b> – each assertion applied to a realistic
 *       {@link BankAccount} scenario so the reader sees WHY each assertion
 *       is useful in production tests.</li>
 * </ol>
 *
 * <h2>JUnit 4 Assertion Quick Reference</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Assertion</th><th>Passes when</th></tr>
 *   <tr><td>assertEquals(expected, actual)</td><td>expected.equals(actual)</td></tr>
 *   <tr><td>assertEquals(expected, actual, delta)</td><td>|expected-actual| &lt;= delta (doubles)</td></tr>
 *   <tr><td>assertTrue(condition)</td><td>condition == true</td></tr>
 *   <tr><td>assertFalse(condition)</td><td>condition == false</td></tr>
 *   <tr><td>assertNull(object)</td><td>object == null</td></tr>
 *   <tr><td>assertNotNull(object)</td><td>object != null</td></tr>
 *   <tr><td>assertSame(expected, actual)</td><td>expected == actual (same reference)</td></tr>
 *   <tr><td>assertNotSame(unexpected, actual)</td><td>unexpected != actual</td></tr>
 *   <tr><td>assertArrayEquals(expected, actual)</td><td>arrays are element-wise equal</td></tr>
 * </table>
 */
public class AssertionsTest {

    // =========================================================================
    // PART 1 — Exact solution code from the exercise specification
    // =========================================================================

    /**
     * Reproduces the exact solution code from Exercise 3 verbatim.
     * Demonstrates: assertEquals, assertTrue, assertFalse, assertNull, assertNotNull.
     */
    @Test
    public void testAssertions() {
        // Assert equals
        assertEquals(5, 2 + 3);

        // Assert true
        assertTrue(5 > 3);

        // Assert false
        assertFalse(5 < 3);

        // Assert null
        assertNull(null);

        // Assert not null
        assertNotNull(new Object());
    }

    // =========================================================================
    // PART 2 — Extended assertions using BankAccount (realistic context)
    // =========================================================================

    private BankAccount account;

    @Before
    public void setUp() {
        // Create a fresh BankAccount before each test
        account = new BankAccount("ACC-001", "Alice Sharma", 1000.00);
    }

    // -------------------------------------------------------------------------
    // assertEquals – numeric equality
    // -------------------------------------------------------------------------

    /**
     * assertEquals(String message, long expected, long actual)
     * Verifies that deposit correctly increases the balance by the exact amount.
     */
    @Test
    public void testAssertEquals_BalanceAfterDeposit() {
        account.deposit(500.00);
        assertEquals("Balance should be 1500 after depositing 500",
                1500.00, account.getBalance(), 0.001);
    }

    /**
     * assertEquals on String values — compares account owner name.
     */
    @Test
    public void testAssertEquals_StringOwnerName() {
        assertEquals("Account owner should be Alice Sharma",
                "Alice Sharma", account.getOwner());
    }

    /**
     * assertEquals with delta for floating-point arithmetic —
     * verifies balance after withdrawal does not drift due to IEEE 754.
     */
    @Test
    public void testAssertEquals_BalanceAfterWithdrawal_WithDelta() {
        account.withdraw(333.33);
        // Delta of 0.001 accommodates floating-point rounding
        assertEquals("Balance should be ~666.67 after withdrawing 333.33",
                666.67, account.getBalance(), 0.001);
    }

    // -------------------------------------------------------------------------
    // assertTrue / assertFalse – boolean conditions
    // -------------------------------------------------------------------------

    /**
     * assertTrue – account has funds after a deposit.
     */
    @Test
    public void testAssertTrue_AccountHasFundsAfterDeposit() {
        account.deposit(100.00);
        assertTrue("Account should report hasFunds() = true when balance > 0",
                account.hasFunds());
    }

    /**
     * assertFalse – account reports no funds after withdrawing entire balance.
     */
    @Test
    public void testAssertFalse_AccountHasNoFundsAfterFullWithdrawal() {
        account.withdraw(1000.00);   // withdraw the entire opening balance
        assertFalse("Account should report hasFunds() = false when balance = 0",
                account.hasFunds());
    }

    /**
     * assertTrue – balance is greater than a threshold.
     */
    @Test
    public void testAssertTrue_BalanceExceedsMinimum() {
        assertTrue("Opening balance of 1000 should be > 500",
                account.getBalance() > 500);
    }

    // -------------------------------------------------------------------------
    // assertNull / assertNotNull – reference checks
    // -------------------------------------------------------------------------

    /**
     * assertNull – no overdraft facility is set by default.
     */
    @Test
    public void testAssertNull_NoOverdraftFacilityByDefault() {
        assertNull("getOverdraftFacility() should return null when none is configured",
                account.getOverdraftFacility());
    }

    /**
     * assertNotNull – a newly created BankAccount is never null.
     */
    @Test
    public void testAssertNotNull_AccountObjectIsNotNull() {
        assertNotNull("BankAccount object should not be null after construction",
                account);
    }

    /**
     * assertNotNull – getSummary() always returns a non-null string.
     */
    @Test
    public void testAssertNotNull_SummaryStringIsNotNull() {
        assertNotNull("getSummary() must never return null",
                account.getSummary());
    }

    // -------------------------------------------------------------------------
    // assertSame / assertNotSame – reference identity
    // -------------------------------------------------------------------------

    /**
     * assertSame – the same BankAccount reference is returned by a helper
     * (no defensive copy; tests object identity, not equality).
     */
    @Test
    public void testAssertSame_SameReferenceReturned() {
        BankAccount reference = account;
        assertSame("Both variables should point to the exact same BankAccount instance",
                reference, account);
    }

    /**
     * assertNotSame – two independently constructed accounts are different objects
     * even if they have the same opening balance.
     */
    @Test
    public void testAssertNotSame_TwoSeparateAccountsAreDistinctObjects() {
        BankAccount anotherAccount = new BankAccount("ACC-002", "Bob Patel", 1000.00);
        assertNotSame("Two separately constructed BankAccount instances must be different objects",
                account, anotherAccount);
    }

    // -------------------------------------------------------------------------
    // assertArrayEquals – array equality
    // -------------------------------------------------------------------------

    /**
     * assertArrayEquals(int[]) – verifies that a fixed interest schedule
     * (modelled as an int array) matches the expected values element-by-element.
     */
    @Test
    public void testAssertArrayEquals_IntArray() {
        int[] expectedMonths = {1, 2, 3, 4, 5, 6};
        int[] actualMonths   = {1, 2, 3, 4, 5, 6};
        assertArrayEquals("Monthly payment schedule arrays should be identical",
                expectedMonths, actualMonths);
    }

    /**
     * assertArrayEquals(double[], delta) – verifies calculated interest amounts
     * match expected values within a floating-point tolerance.
     */
    @Test
    public void testAssertArrayEquals_DoubleArray_WithDelta() {
        // Simulated: 1% monthly interest applied to 1000, 2000, 3000
        double[] expected = {10.00, 20.00, 30.00};
        double[] actual   = {
            1000.00 * 0.01,
            2000.00 * 0.01,
            3000.00 * 0.01
        };
        assertArrayEquals("Interest amounts must match within 0.001 tolerance",
                expected, actual, 0.001);
    }

    /**
     * assertArrayEquals(String[]) – verifies a list of account number strings.
     */
    @Test
    public void testAssertArrayEquals_StringArray() {
        String[] expectedAccounts = {"ACC-001", "ACC-002", "ACC-003"};
        String[] actualAccounts   = {"ACC-001", "ACC-002", "ACC-003"};
        assertArrayEquals("Account number arrays should match exactly",
                expectedAccounts, actualAccounts);
    }

    // -------------------------------------------------------------------------
    // Exception testing using @Test(expected = ...)
    // -------------------------------------------------------------------------

    /**
     * @Test(expected) – JUnit 4's way to assert that a specific exception is thrown.
     * Withdrawing more than the balance must throw IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void testWithdraw_InsufficientFunds_ThrowsIllegalStateException() {
        account.withdraw(9999.00);   // exceeds balance of 1000
    }

    /**
     * @Test(expected) – depositing a negative amount must throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeposit_NegativeAmount_ThrowsIllegalArgumentException() {
        account.deposit(-50.00);
    }
}
