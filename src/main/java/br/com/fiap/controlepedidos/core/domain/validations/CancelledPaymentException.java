package br.com.fiap.controlepedidos.core.domain.validations;

public class CancelledPaymentException extends RuntimeException {
    public CancelledPaymentException(String message) {
        super(message);
    }
}
