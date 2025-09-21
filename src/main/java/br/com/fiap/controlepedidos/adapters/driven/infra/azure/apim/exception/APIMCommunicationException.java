package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim.exception;

public class APIMCommunicationException extends RuntimeException {

    public APIMCommunicationException(String message) {
        super(message);
    }

    public APIMCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
