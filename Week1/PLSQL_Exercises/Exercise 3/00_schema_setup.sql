-- =============================================================================
-- FILE    : 00_schema_setup.sql
-- PURPOSE : Create tables and seed test data for Exercise 3 – Stored Procedures
-- Run this script ONCE before executing any scenario script.
-- Compatible with: Oracle Database 11g R2 and later.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- STEP 1: Drop existing tables (idempotent)
-- -----------------------------------------------------------------------------
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Accounts    CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Employees   CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Departments CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Customers   CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

-- -----------------------------------------------------------------------------
-- STEP 2: Customers (lightweight – provides FK target for Accounts)
-- -----------------------------------------------------------------------------
CREATE TABLE Customers (
    CustomerID   NUMBER(10)    NOT NULL,
    Name         VARCHAR2(100) NOT NULL,
    CONSTRAINT   pk_customers  PRIMARY KEY (CustomerID)
);

-- -----------------------------------------------------------------------------
-- STEP 3: Accounts (Scenarios 1 and 3)
--
-- AccountType: 'SAVINGS' | 'CURRENT'
-- Balance    : current available balance (must be >= 0 for non-overdraft accounts)
-- -----------------------------------------------------------------------------
CREATE TABLE Accounts (
    AccountID    NUMBER(10)    NOT NULL,
    CustomerID   NUMBER(10)    NOT NULL,
    AccountType  VARCHAR2(10)  NOT NULL,
    Balance      NUMBER(15, 2) DEFAULT 0 NOT NULL,
    CONSTRAINT   pk_accounts   PRIMARY KEY (AccountID),
    CONSTRAINT   fk_acc_cust   FOREIGN KEY (CustomerID)  REFERENCES Customers (CustomerID),
    CONSTRAINT   chk_acc_type  CHECK (AccountType IN ('SAVINGS', 'CURRENT')),
    CONSTRAINT   chk_balance   CHECK (Balance >= 0)
);

-- -----------------------------------------------------------------------------
-- STEP 4: Departments (Scenario 2)
-- -----------------------------------------------------------------------------
CREATE TABLE Departments (
    DepartmentID   NUMBER(10)    NOT NULL,
    DepartmentName VARCHAR2(100) NOT NULL,
    CONSTRAINT     pk_depts      PRIMARY KEY (DepartmentID)
);

-- -----------------------------------------------------------------------------
-- STEP 5: Employees (Scenario 2)
-- -----------------------------------------------------------------------------
CREATE TABLE Employees (
    EmployeeID   NUMBER(10)    NOT NULL,
    Name         VARCHAR2(100) NOT NULL,
    DepartmentID NUMBER(10)    NOT NULL,
    Salary       NUMBER(12, 2) NOT NULL,
    CONSTRAINT   pk_employees  PRIMARY KEY (EmployeeID),
    CONSTRAINT   fk_emp_dept   FOREIGN KEY (DepartmentID) REFERENCES Departments (DepartmentID),
    CONSTRAINT   chk_salary    CHECK (Salary > 0)
);

-- -----------------------------------------------------------------------------
-- STEP 6: Seed data
-- -----------------------------------------------------------------------------

-- Customers
INSERT INTO Customers VALUES (1, 'Alice Sharma');
INSERT INTO Customers VALUES (2, 'Bob Patel');
INSERT INTO Customers VALUES (3, 'Carol Singh');
INSERT INTO Customers VALUES (4, 'David Mehta');
INSERT INTO Customers VALUES (5, 'Eva Reddy');

-- Accounts (mix of SAVINGS and CURRENT for both scenarios)
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1001, 1, 'SAVINGS',  5000.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1002, 1, 'CURRENT',  1200.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1003, 2, 'SAVINGS', 18000.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1004, 3, 'SAVINGS',  3500.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1005, 3, 'CURRENT',  8750.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1006, 4, 'SAVINGS',   750.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1007, 5, 'SAVINGS', 22000.00);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance) VALUES (1008, 5, 'CURRENT',  4300.00);

-- Departments
INSERT INTO Departments VALUES (10, 'Retail Banking');
INSERT INTO Departments VALUES (20, 'Technology');
INSERT INTO Departments VALUES (30, 'Operations');

-- Employees
INSERT INTO Employees VALUES (101, 'Raj Kumar',   10, 55000.00);
INSERT INTO Employees VALUES (102, 'Priya Nair',  10, 62000.00);
INSERT INTO Employees VALUES (103, 'Sam Watson',  20, 75000.00);
INSERT INTO Employees VALUES (104, 'Neha Gupta',  20, 80000.00);
INSERT INTO Employees VALUES (105, 'Arun Das',    20, 70000.00);
INSERT INTO Employees VALUES (106, 'Lisa Brown',  30, 48000.00);
INSERT INTO Employees VALUES (107, 'Tom George',  30, 52000.00);

COMMIT;

-- Verification
SELECT 'Customers  : ' || COUNT(*) AS info FROM Customers  UNION ALL
SELECT 'Accounts   : ' || COUNT(*)         FROM Accounts   UNION ALL
SELECT 'Departments: ' || COUNT(*)         FROM Departments UNION ALL
SELECT 'Employees  : ' || COUNT(*)         FROM Employees;
