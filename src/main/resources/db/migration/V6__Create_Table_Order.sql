CREATE TABLE orders
(
    order_id     BINARY(16)       NOT NULL,
    order_number INT NOT NULL,
    customer_id  BINARY(16)       NULL,
    cart_id      BINARY(16)       NULL,
    order_status VARCHAR(40) NULL,
    total_cents  INTEGER     NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE SET NULL,
    CONSTRAINT fk_orders_cart
        FOREIGN KEY (cart_id)
            REFERENCES carts (cart_id)
            ON DELETE SET NULL
);

CREATE INDEX idx_orders_order_id ON orders (order_id);
CREATE INDEX idx_orders_cart_id ON orders (cart_id);