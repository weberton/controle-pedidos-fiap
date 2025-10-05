-- Recomendação geral: usar InnoDB para garantir FK, transações e row-level locking.
-- Charset recomendado para suporte a todos os caracteres:
-- DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE customers (
    id BINARY(16) PRIMARY KEY,                -- UUID compactado (16 bytes)
    cpf VARCHAR(11) UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    version BIGINT,                           -- para controle de versão / optimistic locking
    INDEX idx_customers_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- (Preservado conforme seu script — tabela possivelmente duplicada)
CREATE TABLE clientes (
    id BINARY(16) PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    version BIGINT,
    INDEX idx_clientes_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE products (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    price INTEGER NOT NULL,                   -- preço em cents (inteiro)
    category SMALLINT NOT NULL,
    description VARCHAR(256) NOT NULL,
    active BOOLEAN NOT NULL,
    image VARCHAR(256)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE carts (
    cart_id     BINARY(16)       NOT NULL,
    customer_id BINARY(16)       NULL,
    total_cents BIGINT           NOT NULL,
    created_at  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id),
    CONSTRAINT fk_carts_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cart_items (
    cart_item_id   BINARY(16)       NOT NULL PRIMARY KEY,
    cart_id        BINARY(16)       NOT NULL,
    product_id     BINARY(16)       NOT NULL,
    quantity       INT              NOT NULL CHECK (quantity >= 1),
    price_cents    BIGINT           NOT NULL CHECK (price_cents >= 0),
    subtotal_cents BIGINT           NOT NULL CHECK (subtotal_cents >= 0),
    CONSTRAINT fk_cartitems_cart FOREIGN KEY (cart_id)
        REFERENCES carts (cart_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_cartitems_product FOREIGN KEY (product_id)
        REFERENCES products (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_cart_items_cart_id    ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product_id ON cart_items(product_id);

CREATE TABLE orders (
    order_id     BINARY(16)       NOT NULL,
    order_number INT              NOT NULL,
    customer_id  BINARY(16)       NULL,
    cart_id      BINARY(16)       NULL,
    order_status VARCHAR(40)      NULL,
    total_cents  INTEGER          NOT NULL,
    created_at   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE SET NULL,
    CONSTRAINT fk_orders_cart
        FOREIGN KEY (cart_id)
            REFERENCES carts (cart_id)
            ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_orders_order_id ON orders (order_id);
CREATE INDEX idx_orders_cart_id ON orders (cart_id);

CREATE TABLE payments (
    payment_id         BINARY(16)      NOT NULL,
    order_id           BINARY(16)      NOT NULL UNIQUE,
    created_at         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    payment_status     VARCHAR(40)     NOT NULL,
    paymentvalue_cents INTEGER         NOT NULL,
    paymentprovider    VARCHAR(255)    NOT NULL,
    qr_code            VARCHAR(255)    NOT NULL,
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
            REFERENCES orders (order_id)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_payments_payment_id ON payments (payment_id);
CREATE INDEX idx_payments_order_id ON payments (order_id);
