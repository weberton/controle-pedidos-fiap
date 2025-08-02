package br.com.fiap.controlepedidos.core.domain.entities;

import br.com.fiap.controlepedidos.core.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@AttributeOverride(
        name = "id",
        column = @Column(name = "payment_id", nullable = false, updatable = false)
)
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Payment extends AbstractEntity {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "paymentvalue_cents", nullable = false)
    private int totalCents;

    @Column(name = "paymentprovider", nullable = false)
    private String provider;

    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    public Payment() {
        this.prePersist();
    }

    @Override
    public void prePersist() {
        super.prePersist();
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
        this.createPayment();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public void createPayment() {
        this.paymentStatus = PaymentStatus.WAITING;
    }

    public void receivePayment() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void cancelPayment() {
        this.paymentStatus = PaymentStatus.CANCELLED;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTotalCents(int totalCents) {
        this.totalCents = totalCents;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Order getOrder() {
        return order;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public String getProvider() {
        return provider;
    }

    public String getQrCode() {
        return qrCode;
    }
}