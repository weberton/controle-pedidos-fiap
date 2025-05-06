package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MercadoPagoPaymentResponseDTO {


    @JsonProperty("in_store_order_id")
    private String mercadoPagoOrderId;

    @JsonProperty("qr_data")
    private String qrCodeData;

    @JsonProperty("error")
    private String errorTitle;

    @JsonProperty("message")
    private String errorDescription;

    @JsonProperty("status")
    private String statusCode;


    public MercadoPagoPaymentResponseDTO(String mercadoPagoOrderId, String qrCodeData, String errorTitle, String errorDescription, String statusCode) {
        this.mercadoPagoOrderId = mercadoPagoOrderId;
        this.qrCodeData = qrCodeData;
        this.errorTitle = errorTitle;
        this.errorDescription = errorDescription;
        this.statusCode = statusCode;
    }

    public String getMercadoPagoOrderId() {
        return mercadoPagoOrderId;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
