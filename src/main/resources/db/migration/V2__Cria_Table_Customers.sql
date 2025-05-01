CREATE TABLE customers (
    id BINARY(16) PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    version BIGINT
);
CREATE INDEX idx_customers_cpf ON customers(cpf);