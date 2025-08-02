package br.com.fiap.controlepedidos.core.domain.validations;

public class PaymentValueMismatchException extends RuntimeException {
    public PaymentValueMismatchException(String message) {
        super(message);
    }
}
