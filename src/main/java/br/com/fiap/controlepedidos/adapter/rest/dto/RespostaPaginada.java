package br.com.fiap.controlepedidos.adapter.rest.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class RespostaPaginada<T> {
    List<T> content;
    int pageNumber;
    int size;
    long totalElements;
    int totalPages;

    public static <T> RespostaPaginada<T> of(Page<T> page) {
        return new RespostaPaginada<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

}
