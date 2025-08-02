package br.com.fiap.controlepedidos.core.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "carts")
@AttributeOverride(
        name = "id",
        column = @Column(name = "cart_id", nullable = false, updatable = false)
)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Cart extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "total_cents", nullable = false)
    private int totalCents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public void prePersist() {
        super.prePersist();
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
        recalculateTotal();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
        recalculateTotal();
    }

    public int recalculateTotal() {
        this.totalCents = items.stream()
                .mapToInt(CartItem::calculateSubTotalCents)
                .sum();
        return totalCents;
    }

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
        recalculateTotal();
    }

    public void removeItem(CartItem item) {
        if (Objects.nonNull(item)) {
            items.removeIf(cartItem -> cartItem.getId().equals(item.getId()));
            recalculateTotal();
        }
    }

    public void removeItem(UUID itemId) {
        if (Objects.nonNull(itemId)) {
            CartItem cartItem = new CartItem();
            cartItem.setId(itemId);

            removeItem(cartItem);
            recalculateTotal();
        }
    }

    public void clearItems() {
        items.forEach(i -> i.setCart(null));
        items.clear();
        recalculateTotal();
    }

    public void updateItemQuantity(UUID itemId, int quantity) {
        if (quantity == 0) {
            removeItem(itemId);
        }

        items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    recalculateTotal();
                });
    }
}