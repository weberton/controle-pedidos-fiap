package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
        UUID id,
        String customerName,
        String status,
        int orderNumber
) {
    public static OrderDTO convertToDTO(Order order, Customer customer) {
        String customerName = (customer != null && customer.getName() != null) ? customer.getName() : "N/A";
        return new OrderDTO(
                order.getId(),
                customerName,
                order.getOrderStatus().getDescription(),
                order.getOrderNumber()
        );
    }
}