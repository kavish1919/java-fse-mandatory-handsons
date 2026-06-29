-- =============================================================================
-- Exercise 6: PL/SQL Stored Procedures
-- Cognizant Digital Nurture 5.0 — Week 2
--
-- Demonstrates:
--   Stored procedures with IN / OUT parameters
--   DML within procedures (INSERT, UPDATE)
--   COMMIT / ROLLBACK
--   Exception handling inside procedures
--   Calling procedures from anonymous blocks
--
-- Pre-requisites (run once to create the demonstration tables):
--   Run the SETUP section below before executing the procedures.
-- =============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED;

-- =============================================================================
-- SETUP: Create demonstration tables
-- =============================================================================

-- Drop tables if they already exist (idempotent re-run)
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE order_audit_log';
EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE orders';
EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE employees';
EXCEPTION WHEN OTHERS THEN NULL; END;
/

CREATE TABLE employees (
    emp_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name    VARCHAR2(50)   NOT NULL,
    last_name     VARCHAR2(50)   NOT NULL,
    department    VARCHAR2(50)   NOT NULL,
    salary        NUMBER(10, 2)  NOT NULL,
    hire_date     DATE           DEFAULT SYSDATE
);

CREATE TABLE orders (
    order_id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_name VARCHAR2(100)  NOT NULL,
    product_code  VARCHAR2(20)   NOT NULL,
    quantity      NUMBER         NOT NULL,
    unit_price    NUMBER(10, 2)  NOT NULL,
    status        VARCHAR2(20)   DEFAULT 'PENDING',
    order_date    DATE           DEFAULT SYSDATE,
    CONSTRAINT chk_order_qty    CHECK (quantity > 0),
    CONSTRAINT chk_order_price  CHECK (unit_price > 0),
    CONSTRAINT chk_order_status CHECK (status IN ('PENDING','CONFIRMED','SHIPPED','CANCELLED'))
);

CREATE TABLE order_audit_log (
    log_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id      NUMBER,
    old_status    VARCHAR2(20),
    new_status    VARCHAR2(20),
    changed_by    VARCHAR2(50),
    changed_at    TIMESTAMP DEFAULT SYSTIMESTAMP
);

-- Seed data
INSERT INTO employees (first_name, last_name, department, salary)
    VALUES ('Alice', 'Johnson', 'Engineering', 85000);
INSERT INTO employees (first_name, last_name, department, salary)
    VALUES ('Bob',   'Smith',   'Marketing',   62000);
INSERT INTO employees (first_name, last_name, department, salary)
    VALUES ('Carol', 'Williams','HR',           55000);
COMMIT;

INSERT INTO orders (customer_name, product_code, quantity, unit_price)
    VALUES ('TechCorp Ltd',   'LAPTOP-X1',  2,  1299.99);
INSERT INTO orders (customer_name, product_code, quantity, unit_price)
    VALUES ('RetailHub',      'MONITOR-27', 5,   349.00);
INSERT INTO orders (customer_name, product_code, quantity, unit_price)
    VALUES ('StartupInc',     'KEYBOARD-M', 10,   89.95);
COMMIT;

-- =============================================================================
-- PROCEDURE 1: add_employee
--   IN  : p_first_name, p_last_name, p_department, p_salary
--   OUT : p_new_emp_id, p_status_msg
-- =============================================================================
CREATE OR REPLACE PROCEDURE add_employee (
    p_first_name  IN  employees.first_name%TYPE,
    p_last_name   IN  employees.last_name%TYPE,
    p_department  IN  employees.department%TYPE,
    p_salary      IN  employees.salary%TYPE,
    p_new_emp_id  OUT employees.emp_id%TYPE,
    p_status_msg  OUT VARCHAR2
)
AS
BEGIN
    -- Guard: salary must be positive
    IF p_salary <= 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Salary must be a positive number.');
    END IF;

    -- Guard: name fields must not be blank
    IF TRIM(p_first_name) IS NULL OR TRIM(p_last_name) IS NULL THEN
        RAISE_APPLICATION_ERROR(-20002, 'First and last name must not be blank.');
    END IF;

    INSERT INTO employees (first_name, last_name, department, salary)
    VALUES (TRIM(p_first_name), TRIM(p_last_name), TRIM(p_department), p_salary)
    RETURNING emp_id INTO p_new_emp_id;

    COMMIT;
    p_status_msg := 'SUCCESS: Employee added with ID ' || p_new_emp_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        p_new_emp_id := NULL;
        p_status_msg := 'ERROR: ' || SQLERRM;
END add_employee;
/

-- =============================================================================
-- PROCEDURE 2: update_order_status
--   IN  : p_order_id, p_new_status, p_changed_by
--   OUT : p_rows_updated, p_status_msg
-- =============================================================================
CREATE OR REPLACE PROCEDURE update_order_status (
    p_order_id     IN  orders.order_id%TYPE,
    p_new_status   IN  orders.status%TYPE,
    p_changed_by   IN  VARCHAR2,
    p_rows_updated OUT NUMBER,
    p_status_msg   OUT VARCHAR2
)
AS
    v_old_status   orders.status%TYPE;
BEGIN
    -- Fetch current status; raises NO_DATA_FOUND if order does not exist
    SELECT status INTO v_old_status
    FROM   orders
    WHERE  order_id = p_order_id;

    -- Prevent transitioning to the same status
    IF v_old_status = p_new_status THEN
        p_rows_updated := 0;
        p_status_msg   := 'WARNING: Order ' || p_order_id
                        || ' is already in status ''' || p_new_status || '''.';
        RETURN;
    END IF;

    -- Perform the update
    UPDATE orders
    SET    status = p_new_status
    WHERE  order_id = p_order_id;

    p_rows_updated := SQL%ROWCOUNT;

    -- Write audit log entry
    INSERT INTO order_audit_log (order_id, old_status, new_status, changed_by)
    VALUES (p_order_id, v_old_status, p_new_status, p_changed_by);

    COMMIT;
    p_status_msg := 'SUCCESS: Order ' || p_order_id
                  || ' updated from ''' || v_old_status
                  || ''' to ''' || p_new_status || '''.';

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        p_rows_updated := 0;
        p_status_msg   := 'ERROR: Order ID ' || p_order_id || ' does not exist.';
    WHEN OTHERS THEN
        ROLLBACK;
        p_rows_updated := 0;
        p_status_msg   := 'ERROR: ' || SQLERRM;
END update_order_status;
/

-- =============================================================================
-- PROCEDURE 3: apply_department_raise
--   IN  : p_department, p_raise_pct
--   OUT : p_employees_updated, p_status_msg
-- =============================================================================
CREATE OR REPLACE PROCEDURE apply_department_raise (
    p_department        IN  employees.department%TYPE,
    p_raise_pct         IN  NUMBER,
    p_employees_updated OUT NUMBER,
    p_status_msg        OUT VARCHAR2
)
AS
BEGIN
    IF p_raise_pct <= 0 OR p_raise_pct > 50 THEN
        RAISE_APPLICATION_ERROR(-20003,
            'Raise percentage must be between 0 and 50; got: ' || p_raise_pct);
    END IF;

    UPDATE employees
    SET    salary = salary * (1 + p_raise_pct / 100)
    WHERE  department = p_department;

    p_employees_updated := SQL%ROWCOUNT;

    IF p_employees_updated = 0 THEN
        ROLLBACK;
        p_status_msg := 'WARNING: No employees found in department ''' || p_department || '''.';
    ELSE
        COMMIT;
        p_status_msg := 'SUCCESS: Applied ' || p_raise_pct || '% raise to '
                      || p_employees_updated || ' employee(s) in ' || p_department || '.';
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        p_employees_updated := 0;
        p_status_msg := 'ERROR: ' || SQLERRM;
END apply_department_raise;
/

-- =============================================================================
-- TEST CALLS
-- =============================================================================

-- -----------------------------------------------------------------------
-- Test 1: Add a valid employee
-- -----------------------------------------------------------------------
DECLARE
    v_id  employees.emp_id%TYPE;
    v_msg VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 1: add_employee (valid) ===');
    add_employee('Diana', 'Prince', 'Engineering', 92000, v_id, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
    DBMS_OUTPUT.PUT_LINE('New Employee ID: ' || v_id);
END;
/

-- -----------------------------------------------------------------------
-- Test 2: Add employee with invalid salary (should roll back + error)
-- -----------------------------------------------------------------------
DECLARE
    v_id  employees.emp_id%TYPE;
    v_msg VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 2: add_employee (invalid salary) ===');
    add_employee('Eve', 'Adams', 'Finance', -500, v_id, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
END;
/

-- -----------------------------------------------------------------------
-- Test 3: Update existing order status
-- -----------------------------------------------------------------------
DECLARE
    v_rows NUMBER;
    v_msg  VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 3: update_order_status (valid) ===');
    update_order_status(1, 'CONFIRMED', 'SYSTEM_JOB', v_rows, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
    DBMS_OUTPUT.PUT_LINE('Rows updated: ' || v_rows);
END;
/

-- -----------------------------------------------------------------------
-- Test 4: Update non-existent order (should return error message)
-- -----------------------------------------------------------------------
DECLARE
    v_rows NUMBER;
    v_msg  VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 4: update_order_status (bad order ID) ===');
    update_order_status(9999, 'SHIPPED', 'ADMIN', v_rows, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
END;
/

-- -----------------------------------------------------------------------
-- Test 5: Apply department-wide salary raise
-- -----------------------------------------------------------------------
DECLARE
    v_count NUMBER;
    v_msg   VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 5: apply_department_raise (Engineering, 10%) ===');
    apply_department_raise('Engineering', 10, v_count, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
END;
/

-- -----------------------------------------------------------------------
-- Test 6: Apply raise to non-existent department
-- -----------------------------------------------------------------------
DECLARE
    v_count NUMBER;
    v_msg   VARCHAR2(200);
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test 6: apply_department_raise (unknown dept) ===');
    apply_department_raise('Legal', 5, v_count, v_msg);
    DBMS_OUTPUT.PUT_LINE(v_msg);
END;
/

-- -----------------------------------------------------------------------
-- Verify audit log
-- -----------------------------------------------------------------------
SELECT log_id, order_id, old_status, new_status, changed_by, changed_at
FROM   order_audit_log
ORDER  BY log_id;
