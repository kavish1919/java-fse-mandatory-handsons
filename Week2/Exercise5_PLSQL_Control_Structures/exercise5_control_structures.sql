-- =============================================================================
-- Exercise 5: PL/SQL Control Structures
-- Cognizant Digital Nurture 5.0 — Week 2
--
-- Demonstrates:
--   IF / ELSIF / ELSE
--   CASE expression and CASE statement
--   FOR loop
--   WHILE loop
--   LOOP … EXIT WHEN
--   Nested loops
--   Exception handling (predefined + user-defined)
--
-- Run with: @exercise5_control_structures.sql
-- Tested on: Oracle Database 19c / 21c
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

-- ---------------------------------------------------------------------------
-- SECTION 1: IF / ELSIF / ELSE — Employee classification by salary band
-- ---------------------------------------------------------------------------
DECLARE
    v_salary      NUMBER := 75000;
    v_band        VARCHAR2(20);
    v_bonus_pct   NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 1: IF-ELSIF-ELSE ===');

    IF v_salary < 30000 THEN
        v_band      := 'Junior';
        v_bonus_pct := 5;
    ELSIF v_salary BETWEEN 30000 AND 59999 THEN
        v_band      := 'Mid-Level';
        v_bonus_pct := 10;
    ELSIF v_salary BETWEEN 60000 AND 99999 THEN
        v_band      := 'Senior';
        v_bonus_pct := 15;
    ELSE
        v_band      := 'Executive';
        v_bonus_pct := 20;
    END IF;

    DBMS_OUTPUT.PUT_LINE('Salary  : ' || v_salary);
    DBMS_OUTPUT.PUT_LINE('Band    : ' || v_band);
    DBMS_OUTPUT.PUT_LINE('Bonus % : ' || v_bonus_pct || '%');
    DBMS_OUTPUT.PUT_LINE('Bonus   : ' || (v_salary * v_bonus_pct / 100));
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 2: CASE Statement — Grade evaluation
-- ---------------------------------------------------------------------------
DECLARE
    v_score VARCHAR2(2) := 'B';
    v_desc  VARCHAR2(50);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 2: CASE Statement ===');

    CASE v_score
        WHEN 'A' THEN v_desc := 'Outstanding';
        WHEN 'B' THEN v_desc := 'Exceeds Expectations';
        WHEN 'C' THEN v_desc := 'Meets Expectations';
        WHEN 'D' THEN v_desc := 'Needs Improvement';
        WHEN 'F' THEN v_desc := 'Unsatisfactory';
        ELSE           v_desc := 'Unknown Grade';
    END CASE;

    DBMS_OUTPUT.PUT_LINE('Score       : ' || v_score);
    DBMS_OUTPUT.PUT_LINE('Description : ' || v_desc);
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 3: CASE Expression — Numeric range (searched CASE)
-- ---------------------------------------------------------------------------
DECLARE
    v_temperature NUMBER := 38.5;
    v_status      VARCHAR2(30);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 3: Searched CASE Expression ===');

    v_status := CASE
        WHEN v_temperature < 36.1 THEN 'Hypothermia Risk'
        WHEN v_temperature BETWEEN 36.1 AND 37.2 THEN 'Normal'
        WHEN v_temperature BETWEEN 37.3 AND 38.0 THEN 'Low-Grade Fever'
        WHEN v_temperature BETWEEN 38.1 AND 39.0 THEN 'Moderate Fever'
        ELSE 'High Fever — Seek Immediate Care'
    END;

    DBMS_OUTPUT.PUT_LINE('Temperature : ' || v_temperature || ' °C');
    DBMS_OUTPUT.PUT_LINE('Status      : ' || v_status);
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 4: FOR Loop — Multiplication table
-- ---------------------------------------------------------------------------
DECLARE
    v_base NUMBER := 7;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 4: FOR Loop — Multiplication Table ===');
    FOR i IN 1..12 LOOP
        DBMS_OUTPUT.PUT_LINE(v_base || ' x ' || i || ' = ' || (v_base * i));
    END LOOP;
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 5: WHILE Loop — Compound interest until target
-- ---------------------------------------------------------------------------
DECLARE
    v_balance NUMBER := 1000;
    v_rate    NUMBER := 0.08;
    v_target  NUMBER := 2000;
    v_year    NUMBER := 0;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 5: WHILE Loop — Investment Growth ===');
    DBMS_OUTPUT.PUT_LINE('Start: £' || v_balance || ' | Target: £' || v_target);

    WHILE v_balance < v_target LOOP
        v_year    := v_year + 1;
        v_balance := v_balance * (1 + v_rate);
        DBMS_OUTPUT.PUT_LINE('Year ' || LPAD(v_year, 2, ' ')
            || ': £' || ROUND(v_balance, 2));
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Target reached in ' || v_year || ' years.');
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 6: LOOP … EXIT WHEN — Fibonacci sequence
-- ---------------------------------------------------------------------------
DECLARE
    v_a   NUMBER := 0;
    v_b   NUMBER := 1;
    v_tmp NUMBER;
    v_max NUMBER := 200;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 6: LOOP-EXIT WHEN — Fibonacci <=  ' || v_max || ' ===');

    LOOP
        EXIT WHEN v_a > v_max;
        DBMS_OUTPUT.PUT_LINE(v_a);
        v_tmp := v_a + v_b;
        v_a   := v_b;
        v_b   := v_tmp;
    END LOOP;
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 7: Nested Loops — Triangle pattern
-- ---------------------------------------------------------------------------
DECLARE
    v_line VARCHAR2(50);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 7: Nested Loops — Star Triangle ===');
    FOR row IN 1..6 LOOP
        v_line := '';
        FOR col IN 1..row LOOP
            v_line := v_line || '* ';
        END LOOP;
        DBMS_OUTPUT.PUT_LINE(v_line);
    END LOOP;
END;
/

-- ---------------------------------------------------------------------------
-- SECTION 8: Exception Handling
-- ---------------------------------------------------------------------------
DECLARE
    v_divisor    NUMBER := 0;
    v_result     NUMBER;
    e_negative   EXCEPTION;   -- user-defined exception
    v_input      NUMBER := -5;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 8: Exception Handling ===');

    -- Trigger user-defined exception
    IF v_input < 0 THEN
        RAISE e_negative;
    END IF;

    -- Trigger predefined exception (ZERO_DIVIDE)
    v_result := 100 / v_divisor;

EXCEPTION
    WHEN e_negative THEN
        DBMS_OUTPUT.PUT_LINE('Error: Negative input detected — value: ' || v_input);
    WHEN ZERO_DIVIDE THEN
        DBMS_OUTPUT.PUT_LINE('Error: Division by zero is not permitted.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Unexpected error: ' || SQLERRM);
END;
/

-- Null input demonstration
DECLARE
    v_name   VARCHAR2(50) := NULL;
    v_length NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== SECTION 8b: NULL Input Handling ===');

    IF v_name IS NULL THEN
        DBMS_OUTPUT.PUT_LINE('Warning: name is NULL — defaulting to ''Unknown''.');
        v_name := 'Unknown';
    END IF;

    v_length := LENGTH(v_name);
    DBMS_OUTPUT.PUT_LINE('Name   : ' || v_name);
    DBMS_OUTPUT.PUT_LINE('Length : ' || v_length);
END;
/
