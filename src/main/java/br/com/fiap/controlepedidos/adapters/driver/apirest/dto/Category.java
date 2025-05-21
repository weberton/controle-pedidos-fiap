package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

public enum Category {

    LANCHE(1, "Lanche"),
    BEBIDA(2, "Bebida"),
    ACOMPANHAMENTO(3, "Acompanhamento"),
    SOBREMESA(4, "Sobremesa");

    private final int id;
    private final String descricao;

    Category(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}
