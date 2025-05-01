package br.com.fiap.controlepedidos.core.domain.validations;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
