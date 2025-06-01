package br.com.fiap.controlepedidos.core.domain.entities;

import br.com.fiap.controlepedidos.core.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Random;

@Entity
@Table(name = "orders")
@AttributeOverride(
        name = "id",
        column = @Column(name = "order_id", nullable = false, updatable = false)
)
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Order extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "total_cents", nullable = false)
    private int totalCents;

    @Column(name = "order_number", nullable = false)
    private int orderNumber;


    public Order() {
        prePersist();
    }

    @Override
    public void prePersist() {
        super.prePersist();
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
        this.createOrder();
    }


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public void createOrder() {
        this.orderStatus = OrderStatus.CREATED;
        this.orderNumber = generateRandomOrderNumber();
    }

    private int generateRandomOrderNumber() {
        Random random = new Random();
        return random.nextInt(10_000) + 1;
    }

    public boolean payOrder(float value) {
        if (value == totalCents) {
            this.orderStatus = OrderStatus.RECEIVED;
            return true;
        } else {
            return false;
        }
    }

    public void startOrderPreparation() {
        this.orderStatus = OrderStatus.INPREP;
    }

    public void finishOrderPreparation() {
        this.orderStatus = OrderStatus.READY;
    }

    public void finishOrder() {
        this.orderStatus = OrderStatus.DONE;
    }


}
