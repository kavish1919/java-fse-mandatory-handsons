-- =============================================================================
-- FILE    : scenario1_loan_discount.sql
-- EXERCISE: Exercise 1 – Control Structures  |  Scenario 1
-- PURPOSE : Loop through all Customers; for those above 60 years of age,
--           apply a 1% discount to ALL their current loan interest rates.
--
-- Algorithm:
--   FOR each customer IN Customers LOOP
--       age := TRUNC(MONTHS_BETWEEN(SYSDATE, customer.DOB) / 12)
--       IF age > 60 THEN
--           UPDATE Loans SET InterestRate = InterestRate - 1
--            WHERE CustomerID = customer.CustomerID
--              AND InterestRate > 1    -- guard: rate cannot go below 1%
--       END IF
--   END LOOP
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

DECLARE
    -- Cursor to iterate through every customer
    CURSOR cur_customers IS
        SELECT CustomerID,
               Name,
               DOB,
               TRUNC(MONTHS_BETWEEN(SYSDATE, DOB) / 12) AS Age
        FROM   Customers
        ORDER  BY CustomerID;

    v_loans_updated  NUMBER := 0;   -- count of loan rows updated for current customer
    v_total_updated  NUMBER := 0;   -- cumulative count across all eligible customers
    v_eligible_count NUMBER := 0;   -- customers aged > 60

BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Scenario 1: Senior Citizen Loan Interest Discount ===');
    DBMS_OUTPUT.PUT_LINE('Processing customers for 1% interest rate discount...');
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 60, '-'));

    -- -------------------------------------------------------------------------
    -- Main loop: iterate through every customer record
    -- -------------------------------------------------------------------------
    FOR rec IN cur_customers LOOP

        -- Check if the customer is above 60 years of age
        IF rec.Age > 60 THEN

            v_eligible_count := v_eligible_count + 1;

            -- Apply 1% discount to all qualifying loans of this customer.
            -- Guard clause: InterestRate - 1 >= 1 ensures the rate never drops
            -- below 1%, preventing negative or zero rates.
            UPDATE Loans
            SET    InterestRate = InterestRate - 1
            WHERE  CustomerID   = rec.CustomerID
            AND    InterestRate  > 1;   -- only discount if rate allows it

            v_loans_updated  := SQL%ROWCOUNT;
            v_total_updated  := v_total_updated + v_loans_updated;

            DBMS_OUTPUT.PUT_LINE(
                'Customer: '      || RPAD(rec.Name, 20) ||
                ' | Age: '        || rec.Age             ||
                ' | Loans updated: ' || v_loans_updated
            );

        ELSE
            -- Customer is 60 or younger — no action required
            DBMS_OUTPUT.PUT_LINE(
                'Customer: '  || RPAD(rec.Name, 20) ||
                ' | Age: '    || rec.Age             ||
                ' | SKIPPED (age <= 60)'
            );
        END IF;

    END LOOP;

    -- -------------------------------------------------------------------------
    -- Summary
    -- -------------------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 60, '-'));
    DBMS_OUTPUT.PUT_LINE('Eligible customers (age > 60) : ' || v_eligible_count);
    DBMS_OUTPUT.PUT_LINE('Total loan rows updated        : ' || v_total_updated);
    DBMS_OUTPUT.PUT_LINE('=== Scenario 1 complete ===');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
        RAISE;
END;
/

-- Verify the updated rates
SELECT l.LoanID,
       c.Name,
       TRUNC(MONTHS_BETWEEN(SYSDATE, c.DOB) / 12) AS Age,
       l.InterestRate AS UpdatedRate
FROM   Loans     l
JOIN   Customers c ON c.CustomerID = l.CustomerID
ORDER  BY c.CustomerID;
