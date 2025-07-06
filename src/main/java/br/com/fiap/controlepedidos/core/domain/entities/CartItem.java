package br.com.fiap.controlepedidos.core.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@AttributeOverride(
        name = "id",
        column = @Column(name = "cart_item_id", nullable = false, updatable = false)
)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CartItem extends AbstractEntity {

    public Cart getCart() {
        return cart;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getSubtotalCents() {
        return subtotalCents;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_cents", nullable = false)
    private int priceCents;

    @Column(name = "subtotal_cents", nullable = false)
    private int subtotalCents;

    public int calculateSubTotalCents() {
        this.subtotalCents = quantity * priceCents;
        return this.subtotalCents;
    }

    @Override
    public void prePersist() {
        super.prePersist();
        calculateSubTotalCents();
    }

    @PreUpdate
    public void preUpdate() {
        calculateSubTotalCents();
    }
}
