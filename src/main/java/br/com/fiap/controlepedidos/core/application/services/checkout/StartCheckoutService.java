package br.com.fiap.controlepedidos.core.application.services.checkout;

import br.com.fiap.controlepedidos.core.domain.entities.Payment;

import java.util.UUID;

public interface StartCheckoutService {
    public Payment startCheckout(UUID cartId);
}
