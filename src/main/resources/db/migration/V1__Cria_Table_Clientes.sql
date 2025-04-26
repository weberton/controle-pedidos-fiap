CREATE TABLE clientes (
    id BINARY(16) PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    version BIGINT
);
CREATE INDEX idx_clientes_cpf ON clientes(cpf);