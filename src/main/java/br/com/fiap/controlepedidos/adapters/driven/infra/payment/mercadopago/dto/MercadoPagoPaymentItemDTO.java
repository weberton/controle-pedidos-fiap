package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoPaymentItemDTO {

    @JsonProperty("sku_number")
    private String productId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("unit_price")
    private Float productPrice;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("unit_measure")
    private String productUnit;

    @JsonProperty("total_amount")
    private Float totalAmount;
}