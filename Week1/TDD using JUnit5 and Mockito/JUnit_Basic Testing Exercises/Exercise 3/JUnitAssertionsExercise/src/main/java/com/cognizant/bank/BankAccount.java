package com.cognizant.bank;

/**
 * BankAccount – simple domain class used as the Subject Under Test
 * for the JUnit Assertions exercise.
 *
 * <p>Provides deposit, withdraw, and account-info operations whose
 * return values are validated using the full range of JUnit 4 assertions.
 */
public class BankAccount {

    private final String accountNumber;
    private final String owner;
    private double balance;

    /**
     * Creates a BankAccount with a given opening balance.
     *
     * @param accountNumber unique account identifier (non-null)
     * @param owner         account holder name (non-null)
     * @param initialBalance opening balance (must be >= 0)
     */
    public BankAccount(String accountNumber, String owner, double initialBalance) {
        if (accountNumber == null || owner == null) {
            throw new IllegalArgumentException("accountNumber and owner must not be null.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance must be >= 0.");
        }
        this.accountNumber = accountNumber;
        this.owner         = owner;
        this.balance       = initialBalance;
    }

    /**
     * Deposits an amount into the account.
     *
     * @param amount positive amount to deposit
     * @throws IllegalArgumentException if amount <= 0
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws an amount from the account.
     *
     * @param amount positive amount to withdraw
     * @throws IllegalArgumentException if amount <= 0
     * @throws IllegalStateException    if insufficient funds
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > this.balance) {
            throw new IllegalStateException("Insufficient funds.");
        }
        this.balance -= amount;
    }

    /** @return current account balance */
    public double getBalance() { return balance; }

    /** @return the account number string */
    public String getAccountNumber() { return accountNumber; }

    /** @return the account owner's name */
    public String getOwner() { return owner; }

    /**
     * Returns a summary string for display purposes.
     *
     * @return non-null formatted summary
     */
    public String getSummary() {
        return String.format("Account[%s | %s | Balance: %.2f]",
                accountNumber, owner, balance);
    }

    /**
     * Returns {@code true} if the account has a positive balance.
     */
    public boolean hasFunds() {
        return balance > 0;
    }

    /**
     * Returns {@code null} intentionally when no overdraft facility is set,
     * used to demonstrate {@code assertNull}.
     */
    public String getOverdraftFacility() {
        return null;   // no overdraft by default
    }
}
