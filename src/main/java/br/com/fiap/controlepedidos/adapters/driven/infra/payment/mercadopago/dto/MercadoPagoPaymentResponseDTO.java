package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.google.gson.annotations.SerializedName;

public class MercadoPagoPaymentResponseDTO {


    @SerializedName("in_store_order_id")
    private String mercadoPagoOrderId;

    @SerializedName("qr_data")
    private String qrCodeData;

    public MercadoPagoPaymentResponseDTO(String mercadoPagoOrderId, String qrCodeData) {
        this.mercadoPagoOrderId = mercadoPagoOrderId;
        this.qrCodeData = qrCodeData;
    }

    public String getMercadoPagoOrderId() {
        return mercadoPagoOrderId;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }
}
