CREATE TABLE payments
(
    payment_id         BINARY(16)      NOT NULL,
    order_id           BINARY(16)      NOT NULL UNIQUE,
    created_at         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_status     VARCHAR(40)     NOT NULL,
    paymentvalue_cents INTEGER         NOT NULL,
    paymentprovider    VARCHAR(255)    NOT NULL,
    qr_code            VARCHAR(255)    NOT NULL,
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id)
            REFERENCES orders (order_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_payments_payment_id ON payments (payment_id);
CREATE INDEX idx_payments_order_id ON payments (order_id);
