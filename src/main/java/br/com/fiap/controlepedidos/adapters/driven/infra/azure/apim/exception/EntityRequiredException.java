package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception;

public class EntityRequiredException extends RuntimeException {

    public EntityRequiredException(String message) {
        super(message);
    }

    public EntityRequiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
