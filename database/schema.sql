-- USERS TABLE
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    membership_start_date DATETIME,
    membership_end_date DATETIME,
    membership_months INT,
    created_at DATETIME DEFAULT GETDATE()
);

-- BOOKS TABLE
CREATE TABLE books (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    book_name VARCHAR(200),
    book_author VARCHAR(200),
    book_category VARCHAR(100),
    book_status VARCHAR(50),
    taken_by_user_id BIGINT,
    return_date DATETIME
);

-- MEMBERSHIP TABLE
CREATE TABLE membership (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    membership_type VARCHAR(50),
    start_date DATETIME,
    end_date DATETIME
);

-- BOOK TRANSACTION TABLE
CREATE TABLE book_transaction (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    book_id BIGINT,
    issue_date DATETIME,
    return_date DATETIME,
    status VARCHAR(50)
);