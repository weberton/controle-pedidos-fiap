package br.com.fiap.controlepedidos.core.domain;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        this.product = createProduct();
    }

    @Test
    void addItem_whenItemIsAdded_shouldAddItemToCart() {
        //Given
        var item = CartItem.builder()
                .product(product)
                .quantity(3)
                .priceCents(product.getPrice())
                .build();
        var expectedCartTotal = item.getQuantity() * item.getPriceCents();

        //When
        cart.addItem(item);

        //Then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst()).isEqualTo(item);
        assertThat(cart.getTotalCents()).isEqualTo(expectedCartTotal);
    }

    @Test
    void addItem_whenMultipleItemsAdded_shouldAddItemsToCart() {
        //Given
        var item = CartItem.builder()
                .product(product)
                .quantity(3)
                .priceCents(product.getPrice())
                .build();

        //When
        cart.addItem(item);
        cart.addItem(item); //Same item added twice

        var expectedCartTotal = cart.getItems().stream().mapToInt(CartItem::calculateSubTotalCents).sum();

        //Then
        assertThat(cart.getItems()).hasSize(2);
        assertThat(cart.getItems().getFirst()).isEqualTo(item);
        assertThat(cart.getItems().getLast()).isEqualTo(item);
        assertThat(cart.getTotalCents()).isEqualTo(expectedCartTotal);
    }

    @Test
    void removeItem_whenItemIsRemoved_removesItemFromCart() {
        //Given
        var item = CartItem.builder()
                .product(product)
                .quantity(3)
                .priceCents(product.getPrice())
                .build();

        item.setId(UUID.randomUUID());
        //When
        cart.addItem(item);
        cart.removeItem(item);

        //Then
        assertThat(cart.getItems()).isEmpty();
    }

    private Product createProduct() {
        return Product.builder()
                .price(550) //$5,50
                .name("Coca Cola")
                .category(Category.BEBIDA)
                .description("Coca Cola - 2 Litros")
                .build();
    }
}
