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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setTotalCents(int totalCents) {
        this.totalCents = totalCents;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Cart getCart() {
        return cart;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

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
        Random random = new Random(); //NOSONAR
        return random.nextInt(10_000) + 1;
    }

    public boolean payOrder(int value) {
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
