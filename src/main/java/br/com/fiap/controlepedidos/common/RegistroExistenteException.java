package br.com.fiap.controlepedidos.common;

public class RegistroExistenteException extends RuntimeException {
    public RegistroExistenteException(String mensagem) {
        super(mensagem);
    }
}
