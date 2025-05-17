CREATE TABLE carts
(
    cart_id     BINARY(16)       NOT NULL,
    customer_id BINARY(16)       NULL,
    total_cents BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cart_id),
    CONSTRAINT fk_carts_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE SET NULL
);

CREATE TABLE cart_items
(
    cart_item_id   BINARY(16)       NOT NULL PRIMARY KEY,
    cart_id        BINARY(16)       NOT NULL,
    product_id     BINARY(16)       NOT NULL,
    quantity       INT    NOT NULL CHECK (quantity >= 1),
    price_cents    BIGINT NOT NULL CHECK (price_cents >= 0),
    subtotal_cents BIGINT NOT NULL CHECK (subtotal_cents >= 0),
    CONSTRAINT fk_cartitems_cart FOREIGN KEY (cart_id)
        REFERENCES carts (cart_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_cartitems_product FOREIGN KEY (product_id)
        REFERENCES products (id)
);

CREATE INDEX idx_cart_items_cart_id    ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product_id ON cart_items(product_id);