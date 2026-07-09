-- =============================================================================
-- FILE    : scenario1_process_monthly_interest.sql
-- EXERCISE: Exercise 3 – Stored Procedures  |  Scenario 1
-- PURPOSE : Create and execute the stored procedure ProcessMonthlyInterest.
--
-- Procedure logic:
--   For EVERY row in Accounts WHERE AccountType = 'SAVINGS':
--       Balance = Balance + (Balance * 1 / 100)
--             = Balance * 1.01
--
-- Parameters : NONE  (operates on all savings accounts)
-- Returns    : Nothing (side-effect: updates Accounts table, prints summary)
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

-- -----------------------------------------------------------------------------
-- CREATE (or REPLACE) the stored procedure
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest
AS
    -- Cursor: fetch all savings accounts with their current balances
    CURSOR cur_savings IS
        SELECT a.AccountID,
               c.Name        AS CustomerName,
               a.Balance     AS OldBalance
        FROM   Accounts  a
        JOIN   Customers c ON c.CustomerID = a.CustomerID
        WHERE  a.AccountType = 'SAVINGS'
        ORDER  BY a.AccountID;

    v_interest        NUMBER(15, 2);
    v_new_balance     NUMBER(15, 2);
    v_total_interest  NUMBER(15, 2) := 0;
    v_accounts_count  NUMBER        := 0;

    c_interest_rate   CONSTANT NUMBER := 0.01;   -- 1% monthly interest

BEGIN
    DBMS_OUTPUT.PUT_LINE('=== ProcessMonthlyInterest: Monthly Interest Run ===');
    DBMS_OUTPUT.PUT_LINE('Interest rate applied : 1% (0.01) on all SAVINGS accounts');
    DBMS_OUTPUT.PUT_LINE('Run timestamp         : ' || TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS'));
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 72, '-'));

    -- -------------------------------------------------------------------------
    -- Loop through every savings account and apply 1% interest
    -- -------------------------------------------------------------------------
    FOR rec IN cur_savings LOOP

        v_interest    := ROUND(rec.OldBalance * c_interest_rate, 2);
        v_new_balance := rec.OldBalance + v_interest;

        -- Apply the interest credit
        UPDATE Accounts
        SET    Balance = v_new_balance
        WHERE  AccountID = rec.AccountID;

        v_total_interest := v_total_interest + v_interest;
        v_accounts_count := v_accounts_count + 1;

        DBMS_OUTPUT.PUT_LINE(
            'AccountID: ' || LPAD(rec.AccountID, 5)             ||
            '  | Customer: '  || RPAD(rec.CustomerName, 18)     ||
            '  | Old: $'      || TO_CHAR(rec.OldBalance,  'FM99,990.00') ||
            '  | Interest: $' || TO_CHAR(v_interest,      'FM9,990.00')  ||
            '  | New: $'      || TO_CHAR(v_new_balance,   'FM99,990.00')
        );

    END LOOP;

    -- -------------------------------------------------------------------------
    -- Summary and commit
    -- -------------------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 72, '-'));
    DBMS_OUTPUT.PUT_LINE('Accounts processed    : ' || v_accounts_count);
    DBMS_OUTPUT.PUT_LINE('Total interest paid   : $' ||
                          TO_CHAR(v_total_interest, 'FM99,990.00'));
    DBMS_OUTPUT.PUT_LINE('=== ProcessMonthlyInterest complete ===');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR in ProcessMonthlyInterest: ' || SQLERRM);
        RAISE;
END ProcessMonthlyInterest;
/

-- -----------------------------------------------------------------------------
-- TEST EXECUTION
-- -----------------------------------------------------------------------------
-- Show balances BEFORE
SELECT AccountID,
       AccountType,
       TO_CHAR(Balance, 'FM$99,999.00') AS BalanceBefore
FROM   Accounts
WHERE  AccountType = 'SAVINGS'
ORDER  BY AccountID;

-- Execute the procedure
BEGIN
    ProcessMonthlyInterest;
END;
/

-- Show balances AFTER (each should be 1% higher)
SELECT a.AccountID,
       c.Name                                    AS Customer,
       a.AccountType,
       TO_CHAR(a.Balance, 'FM$99,999.00')        AS BalanceAfter
FROM   Accounts  a
JOIN   Customers c ON c.CustomerID = a.CustomerID
WHERE  a.AccountType = 'SAVINGS'
ORDER  BY a.AccountID;
