package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception;

public class MissingRequiredValueException extends RuntimeException {

    public MissingRequiredValueException(String message) {
        super(message);
    }
}