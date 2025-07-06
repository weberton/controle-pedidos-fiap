package br.com.fiap.controlepedidos.core.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @JdbcTypeCode(Types.BINARY)
    private UUID id;
    private String cpf;
    private String name;

    public String getName() {
        return name;
    }

    private String email;

    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public Long getVersion() {
        return version;
    }

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}