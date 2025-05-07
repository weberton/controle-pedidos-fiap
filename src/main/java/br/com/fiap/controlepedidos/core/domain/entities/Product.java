package br.com.fiap.controlepedidos.core.domain.entities;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "products")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @JdbcTypeCode(Types.BINARY)
    private UUID id;
    private String name;
    private String price;
    private Category category;
    private String description;
    private boolean active;
    private String image;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
