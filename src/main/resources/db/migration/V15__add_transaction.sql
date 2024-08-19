CREATE TABLE transactions
(
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER REFERENCES users (id),
    transactionId    VARCHAR(255) NOT NULL,
    amount           DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    type             VARCHAR(10) CHECK (type IN ('DEPOSIT', 'WITHDRAWAL')),
    status           VARCHAR(10) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    description      VARCHAR(255)
);
