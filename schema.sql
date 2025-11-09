-- Schema for Banking-System
CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(64) PRIMARY KEY,
    type VARCHAR(20),
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    phone VARCHAR(50),
    email VARCHAR(255),
    personal_id VARCHAR(100),
    business_registration VARCHAR(100),
    employed BOOLEAN DEFAULT FALSE,
    employer_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(64) PRIMARY KEY,
    customer_id VARCHAR(64) NOT NULL,
    type VARCHAR(50) NOT NULL,
    balance DOUBLE DEFAULT 0.0,
    branch VARCHAR(255),
    initial_deposit DOUBLE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id VARCHAR(128) PRIMARY KEY,
    account_number VARCHAR(64) NOT NULL,
    type VARCHAR(100),
    amount DOUBLE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    balance_after DOUBLE,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
