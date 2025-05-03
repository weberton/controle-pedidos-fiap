package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MercadoPagoPaymentRequestDTO {

    @SerializedName("external_reference")
    private String orderId;

    private String title;
    private String description;

    @SerializedName("notification_url")
    private String callBackUrl;

    @SerializedName("total_amount")
    private Float totalAmount;

    private List<MercadoPagoPaymentItemDTO> items;

    public MercadoPagoPaymentRequestDTO(String orderId, String title, String description,
                                        String callBackUrl, Float totalAmount,
                                        List<MercadoPagoPaymentItemDTO> items) {
        this.orderId = orderId;
        this.title = title;
        this.description = description;
        this.callBackUrl = callBackUrl;
        this.totalAmount = totalAmount;
        this.items = items;
    }

}
