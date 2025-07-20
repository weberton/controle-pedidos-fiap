package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private BigDecimal productPrice;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("unit_measure")
    private String productUnit;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    public BigDecimal convertTotalCentsToDTO(int totalCents) {
        return new BigDecimal(totalCents).divide(new BigDecimal(100));
    }

}