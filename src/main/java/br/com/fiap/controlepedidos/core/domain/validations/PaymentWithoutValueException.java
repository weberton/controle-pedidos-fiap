package br.com.fiap.controlepedidos.core.domain.validations;

public class PaymentWithoutValueException extends RuntimeException {
    public PaymentWithoutValueException(String message) {
        super(message);
    }
}
