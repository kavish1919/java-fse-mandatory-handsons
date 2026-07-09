-- =============================================================================
-- FILE    : scenario3_transfer_funds.sql
-- EXERCISE: Exercise 3 – Stored Procedures  |  Scenario 3
-- PURPOSE : Create and execute the stored procedure TransferFunds.
--
-- Procedure logic:
--   1. Validate that source and target accounts exist.
--   2. Check that source account has sufficient balance.
--   3. Debit source account.
--   4. Credit target account.
--   5. Commit if all steps succeed; ROLLBACK on any error.
--
-- Parameters:
--   p_from_account_id  IN  NUMBER   – source account ID
--   p_to_account_id    IN  NUMBER   – destination account ID
--   p_amount           IN  NUMBER   – amount to transfer (must be > 0)
--
-- Application Errors:
--   -20001  Invalid transfer amount (<= 0)
--   -20002  Source account not found
--   -20003  Target account not found
--   -20004  Cannot transfer to the same account
--   -20005  Insufficient balance in source account
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

-- -----------------------------------------------------------------------------
-- CREATE (or REPLACE) the stored procedure
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account_id  IN  NUMBER,
    p_to_account_id    IN  NUMBER,
    p_amount           IN  NUMBER
)
AS
    v_from_balance    Accounts.Balance%TYPE;
    v_to_balance      Accounts.Balance%TYPE;
    v_from_customer   Customers.Name%TYPE;
    v_to_customer     Customers.Name%TYPE;

BEGIN
    -- -------------------------------------------------------------------------
    -- Step 1 – Input validation
    -- -------------------------------------------------------------------------
    IF p_amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20001,
            'Transfer amount must be greater than zero. Received: ' || p_amount);
    END IF;

    IF p_from_account_id = p_to_account_id THEN
        RAISE_APPLICATION_ERROR(-20004,
            'Source and target accounts must be different.');
    END IF;

    -- -------------------------------------------------------------------------
    -- Step 2 – Lock source account row and verify it exists
    --          SELECT FOR UPDATE ensures no concurrent modification
    -- -------------------------------------------------------------------------
    BEGIN
        SELECT a.Balance, c.Name
        INTO   v_from_balance, v_from_customer
        FROM   Accounts  a
        JOIN   Customers c ON c.CustomerID = a.CustomerID
        WHERE  a.AccountID = p_from_account_id
        FOR UPDATE NOWAIT;   -- fail fast if row is locked by another session
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20002,
                'Source account ID ' || p_from_account_id || ' does not exist.');
    END;

    -- -------------------------------------------------------------------------
    -- Step 3 – Lock target account row and verify it exists
    -- -------------------------------------------------------------------------
    BEGIN
        SELECT a.Balance, c.Name
        INTO   v_to_balance, v_to_customer
        FROM   Accounts  a
        JOIN   Customers c ON c.CustomerID = a.CustomerID
        WHERE  a.AccountID = p_to_account_id
        FOR UPDATE NOWAIT;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20003,
                'Target account ID ' || p_to_account_id || ' does not exist.');
    END;

    -- -------------------------------------------------------------------------
    -- Step 4 – Sufficient balance check
    -- -------------------------------------------------------------------------
    IF v_from_balance < p_amount THEN
        RAISE_APPLICATION_ERROR(-20005,
            'Insufficient balance in account ' || p_from_account_id ||
            '. Available: $' || TO_CHAR(v_from_balance, 'FM99,990.00') ||
            ', Required: $'  || TO_CHAR(p_amount,        'FM99,990.00'));
    END IF;

    DBMS_OUTPUT.PUT_LINE('=== TransferFunds ===');
    DBMS_OUTPUT.PUT_LINE('From : Account ' || p_from_account_id ||
                         ' (' || v_from_customer || ')' ||
                         '  Balance before: $' || TO_CHAR(v_from_balance, 'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE('To   : Account ' || p_to_account_id   ||
                         ' (' || v_to_customer   || ')' ||
                         '  Balance before: $' || TO_CHAR(v_to_balance,   'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE('Amount: $' || TO_CHAR(p_amount, 'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 65, '-'));

    -- -------------------------------------------------------------------------
    -- Step 5 – Debit source account
    -- -------------------------------------------------------------------------
    UPDATE Accounts
    SET    Balance = Balance - p_amount
    WHERE  AccountID = p_from_account_id;

    -- -------------------------------------------------------------------------
    -- Step 6 – Credit target account
    -- -------------------------------------------------------------------------
    UPDATE Accounts
    SET    Balance = Balance + p_amount
    WHERE  AccountID = p_to_account_id;

    -- -------------------------------------------------------------------------
    -- Step 7 – Confirm and commit
    -- -------------------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE('Debit  applied : Account ' || p_from_account_id ||
                         '  New balance: $' ||
                         TO_CHAR(v_from_balance - p_amount, 'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE('Credit applied : Account ' || p_to_account_id   ||
                         '  New balance: $' ||
                         TO_CHAR(v_to_balance   + p_amount, 'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE('Transfer of $' || TO_CHAR(p_amount, 'FM99,990.00') ||
                         ' completed successfully.');
    DBMS_OUTPUT.PUT_LINE('=== TransferFunds complete ===');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR in TransferFunds: ' || SQLERRM);
        RAISE;
END TransferFunds;
/

-- -----------------------------------------------------------------------------
-- TEST EXECUTION
-- -----------------------------------------------------------------------------
-- Show balances before all transfers
SELECT a.AccountID,
       c.Name          AS Customer,
       a.AccountType,
       TO_CHAR(a.Balance, 'FM$99,999.00') AS Balance
FROM   Accounts  a
JOIN   Customers c ON c.CustomerID = a.CustomerID
ORDER  BY a.AccountID;

-- Test 1: Valid transfer — Alice SAVINGS (1001) → Bob SAVINGS (1003), $1,500
BEGIN
    TransferFunds(p_from_account_id => 1001,
                  p_to_account_id   => 1003,
                  p_amount          => 1500);
END;
/

-- Test 2: Valid transfer — Eva CURRENT (1008) → Carol SAVINGS (1004), $800
BEGIN
    TransferFunds(p_from_account_id => 1008,
                  p_to_account_id   => 1004,
                  p_amount          => 800);
END;
/

-- Test 3: Insufficient balance — David SAVINGS (1006) has $750, attempt $2,000
BEGIN
    TransferFunds(p_from_account_id => 1006,
                  p_to_account_id   => 1001,
                  p_amount          => 2000);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Expected error caught: ' || SQLERRM);
END;
/

-- Test 4: Invalid amount — zero transfer
BEGIN
    TransferFunds(p_from_account_id => 1001,
                  p_to_account_id   => 1003,
                  p_amount          => 0);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Expected error caught: ' || SQLERRM);
END;
/

-- Test 5: Same account transfer
BEGIN
    TransferFunds(p_from_account_id => 1001,
                  p_to_account_id   => 1001,
                  p_amount          => 100);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Expected error caught: ' || SQLERRM);
END;
/

-- Show final balances after all successful transfers
SELECT a.AccountID,
       c.Name          AS Customer,
       a.AccountType,
       TO_CHAR(a.Balance, 'FM$99,999.00') AS FinalBalance
FROM   Accounts  a
JOIN   Customers c ON c.CustomerID = a.CustomerID
ORDER  BY a.AccountID;
