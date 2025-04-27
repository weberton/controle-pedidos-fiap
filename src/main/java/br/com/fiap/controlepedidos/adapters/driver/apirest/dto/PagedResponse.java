package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PagedResponse<T> {
    List<T> content;
    int pageNumber;
    int size;
    long totalElements;
    int totalPages;

    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

}
