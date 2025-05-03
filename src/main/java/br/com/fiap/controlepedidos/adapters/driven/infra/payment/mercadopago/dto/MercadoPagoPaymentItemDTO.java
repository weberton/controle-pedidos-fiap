package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.google.gson.annotations.SerializedName;

public class MercadoPagoPaymentItemDTO {

    @SerializedName("sku_number")
    private String productId;

    @SerializedName("category")
    private String category;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("unit_price")
    private Float productPrice;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("unit_measure")
    private String productUnit;

    @SerializedName("total_amount")
    private Float totalAmount;

    public MercadoPagoPaymentItemDTO(String productId, String category, String title, String description,
                                     Float productPrice, Integer quantity, String productUnit, Float totalAmount) {
        this.productId = productId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productUnit = productUnit;
        this.totalAmount = totalAmount;
    }
}