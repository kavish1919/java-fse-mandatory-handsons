-- =============================================================================
-- FILE    : scenario3_loan_reminders.sql
-- EXERCISE: Exercise 1 – Control Structures  |  Scenario 3
-- PURPOSE : Fetch all loans whose DueDate falls within the next 30 days
--           (inclusive of today and exactly 30 days from now) and print a
--           personalised reminder message for each affected customer.
--
-- Algorithm:
--   FOR each loan WHERE DueDate BETWEEN TRUNC(SYSDATE)
--                                   AND TRUNC(SYSDATE) + 30 LOOP
--       PRINT reminder message with customer name, loan ID and days remaining
--   END LOOP
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

DECLARE
    -- Cursor: join Loans with Customers, filter to the 30-day window,
    -- order by urgency (fewest days remaining first)
    CURSOR cur_due_loans IS
        SELECT l.LoanID,
               l.CustomerID,
               c.Name                                        AS CustomerName,
               l.LoanAmount,
               l.InterestRate,
               l.DueDate,
               TRUNC(l.DueDate) - TRUNC(SYSDATE)            AS DaysRemaining
        FROM   Loans     l
        JOIN   Customers c ON c.CustomerID = l.CustomerID
        WHERE  TRUNC(l.DueDate) BETWEEN TRUNC(SYSDATE)
                                    AND TRUNC(SYSDATE) + 30
        ORDER  BY l.DueDate ASC;    -- most urgent first

    v_reminder_count NUMBER := 0;
    v_urgency_label  VARCHAR2(20);

BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Scenario 3: Upcoming Loan Due-Date Reminders ===');
    DBMS_OUTPUT.PUT_LINE('Reference date  : ' || TO_CHAR(SYSDATE, 'DD-MON-YYYY'));
    DBMS_OUTPUT.PUT_LINE('Reminder window : Today to ' ||
                          TO_CHAR(SYSDATE + 30, 'DD-MON-YYYY') || ' (30 days)');
    DBMS_OUTPUT.PUT_LINE(RPAD('=', 70, '='));

    -- -------------------------------------------------------------------------
    -- Main loop: one reminder per qualifying loan
    -- -------------------------------------------------------------------------
    FOR rec IN cur_due_loans LOOP

        v_reminder_count := v_reminder_count + 1;

        -- Assign urgency label based on days remaining
        IF    rec.DaysRemaining = 0 THEN
            v_urgency_label := '*** DUE TODAY ***';
        ELSIF rec.DaysRemaining <= 7 THEN
            v_urgency_label := '!! URGENT !!';
        ELSIF rec.DaysRemaining <= 14 THEN
            v_urgency_label := '! HIGH PRIORITY !';
        ELSE
            v_urgency_label := 'UPCOMING';
        END IF;

        -- Print the reminder message
        DBMS_OUTPUT.PUT_LINE('');
        DBMS_OUTPUT.PUT_LINE('REMINDER [' || v_urgency_label || ']');
        DBMS_OUTPUT.PUT_LINE(
            '  Dear ' || rec.CustomerName || ','
        );
        DBMS_OUTPUT.PUT_LINE(
            '  This is a reminder that your loan (Loan ID: ' || rec.LoanID || ')' ||
            ' of $' || TO_CHAR(rec.LoanAmount, 'FM99,999,990.00') ||
            ' at ' || rec.InterestRate || '% p.a.'
        );
        DBMS_OUTPUT.PUT_LINE(
            '  is due on ' || TO_CHAR(rec.DueDate, 'DD-MON-YYYY') || '.'
        );

        IF rec.DaysRemaining = 0 THEN
            DBMS_OUTPUT.PUT_LINE(
                '  Your payment is due TODAY. Please make the payment immediately to avoid penalties.'
            );
        ELSE
            DBMS_OUTPUT.PUT_LINE(
                '  You have ' || rec.DaysRemaining || ' day(s) remaining to make your payment.'
            );
        END IF;

        DBMS_OUTPUT.PUT_LINE(
            '  Please ensure timely payment to avoid late fees and credit score impact.'
        );
        DBMS_OUTPUT.PUT_LINE(RPAD('-', 70, '-'));

    END LOOP;

    -- -------------------------------------------------------------------------
    -- Summary
    -- -------------------------------------------------------------------------
    DBMS_OUTPUT.PUT_LINE('');
    IF v_reminder_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No loans are due within the next 30 days.');
    ELSE
        DBMS_OUTPUT.PUT_LINE(
            'Total reminders sent: ' || v_reminder_count
        );
    END IF;
    DBMS_OUTPUT.PUT_LINE('=== Scenario 3 complete ===');

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
        RAISE;
END;
/

-- Reference query: show all loans with their due-date status
SELECT l.LoanID,
       c.Name                                     AS CustomerName,
       TO_CHAR(l.DueDate, 'DD-MON-YYYY')          AS DueDate,
       TRUNC(l.DueDate) - TRUNC(SYSDATE)           AS DaysFromToday,
       CASE
           WHEN TRUNC(l.DueDate) BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 30
               THEN 'REMINDER SENT'
           WHEN TRUNC(l.DueDate) < TRUNC(SYSDATE)
               THEN 'OVERDUE'
           ELSE 'FUTURE (>30 days)'
       END                                         AS ReminderStatus
FROM   Loans     l
JOIN   Customers c ON c.CustomerID = l.CustomerID
ORDER  BY l.DueDate;
