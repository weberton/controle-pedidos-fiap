package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.exceptions;

public class MercadoPagoConnectionException extends RuntimeException {

    public MercadoPagoConnectionException(String message, Exception exception) {
        super(message, exception);
    }
}
