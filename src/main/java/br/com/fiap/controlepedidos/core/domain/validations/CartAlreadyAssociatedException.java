package br.com.fiap.controlepedidos.core.domain.validations;

public class CartAlreadyAssociatedException extends RuntimeException {
    public CartAlreadyAssociatedException(String message) {
        super(message);
    }
}
