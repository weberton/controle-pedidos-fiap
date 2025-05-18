package br.com.fiap.controlepedidos.core.application.services.carts.impl;

import br.com.fiap.controlepedidos.core.application.ports.CartsRepository;
import br.com.fiap.controlepedidos.core.application.services.carts.CreateUpdateCartService;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.application.services.product.FindProductByIdService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUpdateCartServiceImpl implements CreateUpdateCartService {

    private final CartsRepository cartsRepository;
    private final FindCartService findCartService;
    private final FindProductByIdService findProductByIdService;

    public CreateUpdateCartServiceImpl(final CartsRepository cartsRepository, FindCartService findCartService,
                                       final FindProductByIdService findProductByIdService) {
        this.cartsRepository = cartsRepository;
        this.findCartService = findCartService;
        this.findProductByIdService = findProductByIdService;
    }

    @Override
    public Cart create(final CartItem cartItem) {
        CartItem newCartItem = newCartItem(cartItem);
        Cart cart = new Cart();
        cart.addItem(newCartItem);
        return this.cartsRepository.save(cart);
    }

    @Override
    public Cart addItem(final UUID cartId,
                        final CartItem cartItem) {
        Cart cart = this.findCartService.findById(cartId);
        cart.addItem(newCartItem(cartItem));
        return this.cartsRepository.save(cart);
    }

    private CartItem newCartItem(final CartItem cartItem) {
        Product product = this.findProductByIdService.findById(cartItem.getProduct().getId());
        return cartItem.toBuilder()
                .priceCents(product.getPrice())
                .product(product).build();
    }

    @Override
    public Cart removeItem(final UUID cartId,
                           final UUID itemId) {
        Cart cart = this.findCartService.findById(cartId);
        cart.removeItem(itemId);

        return cartsRepository.save(cart);
    }

    @Override
    public Cart updateQuantity(final UUID cartId,
                               final UUID itemId,
                               final int quantity) {
        Cart cart = this.findCartService.findById(cartId);
        cart.updateItemQuantity(itemId, quantity);
        return cartsRepository.save(cart);
    }
}
