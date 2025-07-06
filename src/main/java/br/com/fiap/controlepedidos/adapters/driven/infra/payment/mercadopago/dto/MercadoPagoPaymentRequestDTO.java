package br.com.fiap.controlepedidos.adapters.driven.infra.payment.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPaymentRequestDTO {

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setItems(List<MercadoPagoPaymentItemDTO> items) {
        this.items = items;
    }

    @JsonProperty("external_reference")
    private String orderId;

    private String title;
    private String description;

    @JsonProperty("notification_url")
    private String callBackUrl;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private List<MercadoPagoPaymentItemDTO> items;

    public BigDecimal convertTotalCentsToDTO(int totalCents) {
        return new BigDecimal(totalCents).divide(new BigDecimal(100));
    }



}
