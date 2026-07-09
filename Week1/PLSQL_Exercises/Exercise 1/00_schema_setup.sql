-- =============================================================================
-- FILE    : 00_schema_setup.sql
-- PURPOSE : Create tables and insert representative test data for Exercise 1.
-- Run this script ONCE before executing any of the three scenario scripts.
-- Compatible with: Oracle Database 11g R2 and later.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- STEP 1: Drop tables if they already exist (for idempotent re-runs)
-- -----------------------------------------------------------------------------
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE Loans     CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN NULL;   -- ignore "table does not exist"
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE Customers CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN NULL;
END;
/

-- -----------------------------------------------------------------------------
-- STEP 2: Create Customers table
--
-- Columns:
--   CustomerID  – surrogate primary key
--   Name        – full customer name
--   DOB         – date of birth (used to compute age at query time)
--   Balance     – current account balance in USD
--   IsVIP       – VIP flag; updated by Scenario 2 (VARCHAR2 acts as BOOLEAN)
-- -----------------------------------------------------------------------------
CREATE TABLE Customers (
    CustomerID  NUMBER(10)       NOT NULL,
    Name        VARCHAR2(100)    NOT NULL,
    DOB         DATE             NOT NULL,
    Balance     NUMBER(15, 2)    DEFAULT 0    NOT NULL,
    IsVIP       VARCHAR2(5)      DEFAULT 'FALSE' NOT NULL,
    CONSTRAINT  pk_customers PRIMARY KEY (CustomerID),
    CONSTRAINT  chk_balance  CHECK  (Balance >= 0),
    CONSTRAINT  chk_isvip    CHECK  (IsVIP IN ('TRUE', 'FALSE'))
);

-- -----------------------------------------------------------------------------
-- STEP 3: Create Loans table
--
-- Columns:
--   LoanID       – surrogate primary key
--   CustomerID   – foreign key referencing Customers
--   LoanAmount   – principal amount of the loan
--   InterestRate – annual interest rate in percentage (e.g., 8.50 = 8.5%)
--   DueDate      – repayment due date
-- -----------------------------------------------------------------------------
CREATE TABLE Loans (
    LoanID       NUMBER(10)       NOT NULL,
    CustomerID   NUMBER(10)       NOT NULL,
    LoanAmount   NUMBER(15, 2)    NOT NULL,
    InterestRate NUMBER(5, 2)     NOT NULL,
    DueDate      DATE             NOT NULL,
    CONSTRAINT   pk_loans         PRIMARY KEY (LoanID),
    CONSTRAINT   fk_loans_cust    FOREIGN KEY (CustomerID)
                                  REFERENCES  Customers (CustomerID),
    CONSTRAINT   chk_interest     CHECK (InterestRate > 0),
    CONSTRAINT   chk_loanamt      CHECK (LoanAmount   > 0)
);

-- -----------------------------------------------------------------------------
-- STEP 4: Seed Customers
--
-- Mix of ages and balances to exercise all three scenario conditions:
--   - Several customers above 60  (Scenario 1)
--   - Several with Balance > 10000 (Scenario 2)
-- -----------------------------------------------------------------------------
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (1, 'Alice Sharma',    DATE '1955-03-14',  15000.00, 'FALSE'); -- age ~71, VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (2, 'Bob Patel',       DATE '1950-07-22',   8500.00, 'FALSE'); -- age ~75, NOT VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (3, 'Carol Singh',     DATE '1990-11-05',  25000.00, 'FALSE'); -- age ~35, VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (4, 'David Mehta',     DATE '1963-01-30',   9800.00, 'FALSE'); -- age ~62, NOT VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (5, 'Eva Reddy',       DATE '1998-06-18',  11200.00, 'FALSE'); -- age ~27, VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (6, 'Frank Thomas',    DATE '1948-09-09',  32000.00, 'FALSE'); -- age ~77, VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (7, 'Grace Lee',       DATE '2000-12-25',   3000.00, 'FALSE'); -- age ~24, NOT VIP-eligible
INSERT INTO Customers (CustomerID, Name, DOB, Balance, IsVIP) VALUES
    (8, 'Henry Wilson',    DATE '1957-04-11',   7500.00, 'FALSE'); -- age ~68, NOT VIP-eligible

-- -----------------------------------------------------------------------------
-- STEP 5: Seed Loans
--
-- DueDates span a range relative to SYSDATE to exercise Scenario 3:
--   - Some due within next 30 days  → should trigger reminder
--   - Some overdue / far future     → should NOT trigger reminder
-- -----------------------------------------------------------------------------
-- Alice Sharma (CustomerID=1)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (101, 1, 200000.00, 9.50, SYSDATE + 10);   -- due in 10 days  → REMINDER

-- Bob Patel (CustomerID=2)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (102, 2, 150000.00, 8.75, SYSDATE + 25);   -- due in 25 days  → REMINDER

-- Carol Singh (CustomerID=3)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (103, 3,  75000.00, 7.00, SYSDATE + 60);   -- due in 60 days  → no reminder

-- David Mehta (CustomerID=4)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (104, 4, 100000.00, 10.25, SYSDATE - 5);   -- overdue         → no reminder

-- Eva Reddy (CustomerID=5)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (105, 5,  50000.00, 8.00, SYSDATE + 5);    -- due in 5 days   → REMINDER

-- Frank Thomas (CustomerID=6)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (106, 6, 300000.00, 11.00, SYSDATE + 30);  -- due in 30 days  → REMINDER (boundary)

-- Grace Lee (CustomerID=7)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (107, 7,  20000.00, 6.50, SYSDATE + 90);   -- due in 90 days  → no reminder

-- Henry Wilson (CustomerID=8)
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, DueDate) VALUES
    (108, 8, 180000.00, 9.00, SYSDATE + 15);   -- due in 15 days  → REMINDER

COMMIT;

-- Quick verification
SELECT 'Customers inserted: ' || COUNT(*) AS info FROM Customers;
SELECT 'Loans     inserted: ' || COUNT(*) AS info FROM Loans;
