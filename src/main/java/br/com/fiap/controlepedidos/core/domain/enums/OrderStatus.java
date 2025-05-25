package br.com.fiap.controlepedidos.core.domain.enums;

public enum OrderStatus {
    CREATED("Criado"),
    RECEIVED("Recebido"),
    INPREP("Preparação"),
    READY("Pronto"),
    DONE("Finalizado");

    private final String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }
}