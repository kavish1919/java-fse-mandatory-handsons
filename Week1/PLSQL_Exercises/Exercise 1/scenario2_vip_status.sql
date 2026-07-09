-- =============================================================================
-- FILE    : scenario2_vip_status.sql
-- EXERCISE: Exercise 1 – Control Structures  |  Scenario 2
-- PURPOSE : Iterate through all customers; set IsVIP = 'TRUE' for those whose
--           Balance exceeds $10,000, and reset to 'FALSE' for those who no
--           longer qualify (handles balance changes over time).
--
-- Algorithm:
--   FOR each customer IN Customers LOOP
--       IF customer.Balance > 10000 THEN
--           UPDATE Customers SET IsVIP = 'TRUE'
--       ELSE
--           UPDATE Customers SET IsVIP = 'FALSE'
--       END IF
--   END LOOP
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

DECLARE
    -- Cursor to fetch all customers with their current balance
    CURSOR cur_customers IS
        SELECT CustomerID,
               Name,
               Balance,
               IsVIP
        FROM   Customers
        ORDER  BY CustomerID;

    v_vip_count     NUMBER := 0;   -- customers promoted to VIP
    v_non_vip_count NUMBER := 0;   -- customers kept / reset as non-VIP
    v_vip_threshold CONSTANT NUMBER := 10000;   -- promotion threshold in USD

BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Scenario 2: VIP Status Promotion ===');
    DBMS_OUTPUT.PUT_LINE('VIP promotion threshold: $' || v_vip_threshold);
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 65, '-'));

    -- -------------------------------------------------------------------------
    -- Main loop: evaluate every customer's balance
    -- -------------------------------------------------------------------------
    FOR rec IN cur_customers LOOP

        IF rec.Balance > v_vip_threshold THEN
            -- Promote to VIP
            UPDATE Customers
            SET    IsVIP = 'TRUE'
            WHERE  CustomerID = rec.CustomerID;

            v_vip_count := v_vip_count + 1;

            DBMS_OUTPUT.PUT_LINE(
                'Customer: '    || RPAD(rec.Name, 20)             ||
                ' | Balance: $' || TO_CHAR(rec.Balance, 'FM99,999,990.00') ||
                ' | Status: --> VIP PROMOTED'
            );

        ELSE
            -- Not eligible (or revoked if previously VIP)
            UPDATE Customers
            SET    IsVIP = 'FALSE'
            WHERE  CustomerID = rec.CustomerID;

            v_non_vip_count := v_non_vip_count + 1;

            DBMS_OUTPUT.PUT_LINE(
                'Customer: '    || RPAD(rec.Name, 20)             ||
                ' | Balance: $' || TO_CHAR(rec.Balance, 'FM99,999,990.00') ||
                ' | Status: Not eligible'
            );
        END IF;

    END LOOP;

    -- -------------------------------------------------------------------------
    -- Summary
    -- -------------------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE(RPAD('-', 65, '-'));
    DBMS_OUTPUT.PUT_LINE('VIP customers      : ' || v_vip_count);
    DBMS_OUTPUT.PUT_LINE('Non-VIP customers  : ' || v_non_vip_count);
    DBMS_OUTPUT.PUT_LINE('Total processed    : ' || (v_vip_count + v_non_vip_count));
    DBMS_OUTPUT.PUT_LINE('=== Scenario 2 complete ===');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
        RAISE;
END;
/

-- Verify the updated VIP flags
SELECT CustomerID,
       Name,
       TO_CHAR(Balance, 'FM$99,999,990.00') AS Balance,
       IsVIP
FROM   Customers
ORDER  BY IsVIP DESC, Balance DESC;
