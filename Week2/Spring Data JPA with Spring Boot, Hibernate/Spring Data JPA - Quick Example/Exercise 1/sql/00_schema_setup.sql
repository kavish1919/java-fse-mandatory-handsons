-- ===========================================================================
-- Spring Data JPA Hands-On 1: Schema Setup & Sample Data
-- Database Schema: ormlearn
-- ===========================================================================

-- 1. Create Schema
CREATE SCHEMA IF NOT EXISTS ormlearn;
USE ormlearn;

-- 2. Drop table if already exists for clean setup
DROP TABLE IF EXISTS country;

-- 3. Create Country Table
-- Note: Column names co_code and co_name map to entity fields code and name
CREATE TABLE country (
    co_code VARCHAR(2) PRIMARY KEY,
    co_name VARCHAR(50) NOT NULL
);

-- 4. Insert Sample Records
INSERT INTO country (co_code, co_name) VALUES ('IN', 'India');
INSERT INTO country (co_code, co_name) VALUES ('US', 'United States of America');
INSERT INTO country (co_code, co_name) VALUES ('JP', 'Japan');
INSERT INTO country (co_code, co_name) VALUES ('DE', 'Germany');
INSERT INTO country (co_code, co_name) VALUES ('AU', 'Australia');

COMMIT;
