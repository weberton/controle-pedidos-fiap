package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.out.CartItemDTO;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderDTO(
        UUID id,
        String customerName,
        String status,
        int orderNumber,
        List<CartItemDTO> items


) {
    public static OrderDTO convertToDTO(Order order, Customer customer, List<CartItem> items) {
        String customerName = (customer != null && customer.getName() != null) ? customer.getName() : "N/A";

        List<CartItemDTO> itemDTOs = items != null
                ? items.stream().map(CartItemDTO::from).toList()
                : List.of();

        return new OrderDTO(
                order.getId(),
                customerName,
                order.getOrderStatus().getDescription(),
                order.getOrderNumber(),
                itemDTOs
        );
    }
}