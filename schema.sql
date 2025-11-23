-- Schema for Banking-System
-- Updated to match specifications
-- Using quoted identifiers to preserve case sensitivity

CREATE TABLE IF NOT EXISTS customers (
    "customerID" VARCHAR(64) PRIMARY KEY,
    "type" VARCHAR(20) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    email VARCHAR(255),
    phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS IndividualDetails (
    "customerID" VARCHAR(64) PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    "nationalID" VARCHAR(100) NOT NULL,
    employed BOOLEAN DEFAULT FALSE,
    "employerName" VARCHAR(255),
    FOREIGN KEY ("customerID") REFERENCES customers("customerID")
);

CREATE TABLE IF NOT EXISTS CompanyDetails (
    "customerID" VARCHAR(64) PRIMARY KEY,
    "companyName" VARCHAR(255) NOT NULL,
    "regNumber" VARCHAR(100) NOT NULL,
    "contactPerson" VARCHAR(255) NOT NULL,
    FOREIGN KEY ("customerID") REFERENCES customers("customerID")
);

CREATE TABLE IF NOT EXISTS accounts (
    "accountNumber" VARCHAR(64) PRIMARY KEY,
    "customerID" VARCHAR(64) NOT NULL,
    "accountType" VARCHAR(50) NOT NULL,
    balance DOUBLE DEFAULT 0.0,
    branch VARCHAR(255),
    "openDate" DATE NOT NULL,
    FOREIGN KEY ("customerID") REFERENCES customers("customerID")
);

CREATE TABLE IF NOT EXISTS transactions (
    "txnID" VARCHAR(128) PRIMARY KEY,
    "accountNumber" VARCHAR(64) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DOUBLE NOT NULL,
    type VARCHAR(100) NOT NULL,
    FOREIGN KEY ("accountNumber") REFERENCES accounts("accountNumber")
);
