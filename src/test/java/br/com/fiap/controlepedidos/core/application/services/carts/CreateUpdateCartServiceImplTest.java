package br.com.fiap.controlepedidos.core.application.services.carts;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.impl.CreateUpdateCartServiceImpl;
import br.com.fiap.controlepedidos.core.application.services.product.FindProductByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUpdateCartServiceImplTest {

    @Mock
    private FindProductByIdService findProductByIdService;
    @Mock
    private CartsRepository cartsRepository;
    @Mock
    private FindCartService findCartService;
    @InjectMocks
    private CreateUpdateCartServiceImpl cartsService;
    private Product product;

    @BeforeEach
    void setup() {
        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Diet coke")
                .price(500)
                .description("Diet coke is a type of coke that is used to treat diabetes.")
                .active(true)
                .build();
    }

    @Test
    void create_whenCreatingAnonymousCart_shouldSaveCartWithAnonymousCustomer() {
        //Given
        var item1 = CartItem.builder()
                .quantity(2)
                .product(product)
                .build();

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findProductByIdService.findById(product.getId())).thenReturn(product);

        cartsService.create(item1);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());

        Cart capturedCart = cartArgumentCaptor.getValue();
        CartItem capturedItem = capturedCart.getItems().getFirst();

        assertThat(capturedItem.getPriceCents()).isEqualTo(product.getPrice());

        //Verify item subtotal
        assertThat(capturedItem.calculateSubTotalCents())
                .isEqualTo(product.getPrice() * item1.getQuantity());

        //Verify cart total
        assertThat(capturedCart.recalculateTotal())
                .isEqualTo(product.getPrice() * item1.getQuantity());
    }

    @Test
    void create_whenProductDoesNotExist_returnsException() {
        //Given
        var item1 = CartItem.builder()
                .quantity(2)
                .product(product)
                .build();

        when(findProductByIdService.findById(product.getId())).thenThrow(RecordNotFoundException.class);

        //when/then
        assertThrows(RecordNotFoundException.class,
                () -> cartsService.create(item1));
    }

    @Test
    void addItem_WhenNewItemAdded_shouldAddItemToCartAndUpdateSubtotalAndTotal() {
        //Given
        var cartId = UUID.randomUUID();
        var item1 = CartItem.builder()
                .quantity(1)
                .product(product)
                .priceCents(product.getPrice())
                .build();

        var cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item1);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        when(findProductByIdService.findById(product.getId())).thenReturn(product);

        cartsService.addItem(cartId, item1); //sameItem

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());

        Cart capturedCart = cartArgumentCaptor.getValue();
        CartItem capturedItem = capturedCart.getItems().getLast();

        assertThat(capturedItem.getPriceCents()).isEqualTo(product.getPrice());

        //Verify item subtotal
        assertThat(capturedItem.calculateSubTotalCents())
                .isEqualTo(product.getPrice() * item1.getQuantity());

        //Verify cart total
        var expectedCartTotal = cart.getItems().stream().mapToInt(CartItem::calculateSubTotalCents).sum();
        assertThat(capturedCart.recalculateTotal())
                .isEqualTo(expectedCartTotal);
    }

    @Test
    void addItem_WhenCartDoesNotExist_shouldThrowRecordNotFound() {
        //Given
        var cartId = UUID.randomUUID();
        var item1 = CartItem.builder()
                .quantity(1)
                .product(product)
                .priceCents(product.getPrice())
                .build();


        when(findCartService.findById(cartId)).thenThrow(RecordNotFoundException.class);
        //when/then
        assertThrows(RecordNotFoundException.class,
                () -> cartsService.addItem(cartId, item1));

    }

    @Test
    void removeItem_whenItemIsInCart_shouldRemoveItemFromCartAndUpdateSubtotalAndTotal() {
        //Given
        var cartId = UUID.randomUUID();
        var cartItemId1 = UUID.randomUUID();
        var cartItemId2 = UUID.randomUUID();
        var item = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(1)
                .product(product)
                .build();

        var item2 = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(1)
                .product(product)
                .build();

        item.setId(cartItemId1);
        item2.setId(cartItemId2);

        var cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);
        cart.addItem(item2);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        cartsService.removeItem(cartId, cartItemId1);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        var cartUpdated = cartArgumentCaptor.getValue();
        assertThat(cartUpdated.getItems()).hasSize(1);
        assertThat(cartUpdated.getItems().getFirst()).isEqualTo(item2);
        assertThat(cartUpdated.getTotalCents()).isEqualTo(item2.getPriceCents() * item2.getQuantity());
    }

    @Test
    void removeItem_whenItemIsNotInCart_itemsShouldNotBeRemoved() {
        //Given
        var cartId = UUID.randomUUID();
        var cartItemId1 = UUID.randomUUID();
        var invalidCartItemId = UUID.randomUUID();
        var item = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(1)
                .product(product)
                .build();

        item.setId(cartItemId1);

        var cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        cartsService.removeItem(cartId, invalidCartItemId);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        var cartUpdated = cartArgumentCaptor.getValue();
        assertThat(cartUpdated.getItems()).hasSize(1);
        assertThat(cartUpdated.getItems().getFirst()).isEqualTo(item);
        assertThat(cartUpdated.getTotalCents()).isEqualTo(item.getPriceCents() * item.getQuantity());
    }

    @Test
    void updateQuantity_whenQuantityIsUpdatedTo_cartShouldBeEmpty() {
        //Given
        var cartId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var zeroQuantityItem = 0;

        var item = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(2)
                .product(product)
                .build();
        item.setId(itemId);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        cartsService.updateQuantity(cartId, itemId, zeroQuantityItem);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        var cartUpdated = cartArgumentCaptor.getValue();

        assertThat(cartUpdated.getItems()).isEmpty();
        assertThat(cartUpdated.getTotalCents()).isZero();
    }

    @Test
    void updateQuantity_whenItemQuantityIsUpdateToZero_removesOnlyThatItem() {
        //Given
        var cartId = UUID.randomUUID();
        var itemId1 = UUID.randomUUID();
        var itemId2 = UUID.randomUUID();
        var zeroQuantiItem2 = 0;

        var item = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(2)
                .product(product)
                .build();
        item.setId(itemId1);

        var item2 = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(1)
                .product(product)
                .build();
        item2.setId(itemId2);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);
        cart.addItem(item2);

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        cartsService.updateQuantity(cartId, itemId2, zeroQuantiItem2);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        var cartUpdated = cartArgumentCaptor.getValue();

        assertThat(cartUpdated.getItems()).hasSize(1);
        assertThat(cartUpdated.getItems().getFirst()).isEqualTo(item);
        assertThat(cartUpdated.getTotalCents()).isEqualTo(item.getPriceCents() * item.getQuantity());
    }

    @Test
    void updateQuantity_whenItemQuantityIsUpdate_updatedOnlyThatItem() {
        //Given
        var cartId = UUID.randomUUID();
        var itemId1 = UUID.randomUUID();
        var itemId2 = UUID.randomUUID();
        var item1NewQuantity = 1;

        var item = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(2)
                .product(product)
                .build();
        item.setId(itemId1);

        var item2 = CartItem.builder()
                .priceCents(product.getPrice())
                .quantity(1)
                .product(product)
                .build();
        item2.setId(itemId2);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);
        cart.addItem(item2);
        var expectedTotalCents = product.getPrice() * 2; //2 Products - same price

        ArgumentCaptor<Cart> cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        //When
        when(findCartService.findById(cartId)).thenReturn(cart);
        cartsService.updateQuantity(cartId, itemId1, item1NewQuantity);

        //Then
        verify(cartsRepository).save(cartArgumentCaptor.capture());
        var cartUpdated = cartArgumentCaptor.getValue();

        assertThat(cartUpdated.getItems()).hasSize(2);
        assertThat(cartUpdated.getItems().getFirst().getQuantity()).isEqualTo(item1NewQuantity);
        assertThat(cartUpdated.getItems().getLast().getQuantity()).isEqualTo(item2.getQuantity());
        assertThat(cartUpdated.getTotalCents()).isEqualTo(expectedTotalCents);
    }
}
