package br.com.fiap.controlepedidos.core.domain.validations;

public class PaymentProviderNotAllowedException extends RuntimeException {
    public PaymentProviderNotAllowedException(String message) {
        super(message);
    }
}
