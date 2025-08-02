package br.com.fiap.controlepedidos.core.domain.validations;

public class PaymentWithoutOrderException extends RuntimeException {
    public PaymentWithoutOrderException(String message) {
        super(message);
    }
}
