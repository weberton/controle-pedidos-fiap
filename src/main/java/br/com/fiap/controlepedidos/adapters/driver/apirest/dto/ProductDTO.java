package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import br.com.fiap.controlepedidos.core.domain.entities.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDTO(
        UUID id,
        @NotEmpty(message = "Nome não pode ser em branco") String name,
        @NotNull(message = "Preço não pode ser nulo") Integer price,
        @NotNull(message = "Categoria não pode ser nula") Category category,
        @NotEmpty(message = "Descrição não pode ser em branco") String description,
        Boolean active,
        String image
) {

    public Product convertToModel() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .category(this.category)
                .description(this.description)
                //.active(this.active) //TODO Weberton Review
                .active(true)
                .image(this.image)
                .build();
    }

    public static ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getDescription(),
                product.isActive(),
                product.getImage());
    }

}
