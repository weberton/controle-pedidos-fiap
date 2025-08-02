package br.com.fiap.controlepedidos.core.domain.enums;

public enum PaymentStatus {
    WAITING("Aguardando"),
    PAID("Aprovado"),
    CANCELLED("Cancelado");

    private final String desc;

    PaymentStatus(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }
}