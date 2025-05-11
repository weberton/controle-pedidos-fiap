CREATE TABLE products (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category SMALLINT NOT NULL,
    description VARCHAR(256) NOT NULL,
    active BOOLEAN NOT NULL,
    image VARCHAR(256)
);
