package br.com.fiap.controlepedidos.core.domain.validations;

public class ExistentRecordException extends RuntimeException {
    public ExistentRecordException(String message) {
        super(message);
    }
}
