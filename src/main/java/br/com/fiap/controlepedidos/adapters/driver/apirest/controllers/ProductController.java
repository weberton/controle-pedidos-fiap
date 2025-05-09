package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.ProductApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.core.application.services.product.*;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ProductController implements ProductApi {

    private final ICreate createProductService;
    private final IFindById findProductService;
    private final IFindAll findAllProductService;
    private final IUpdate updateProductService;
    private final IDeleteById deleteProductService;

    public ProductController(ICreate createProductService, IFindById findProductService, IFindAll findAllProductService,
                             IUpdate updateProductService, IDeleteById deleteProductService) {
        this.createProductService = createProductService;
        this.findProductService = findProductService;
        this.findAllProductService = findAllProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
    }

    @Override
    public ResponseEntity<ProductDTO> create(ProductDTO productDTO) {
        Product product = createProductService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductDTO.convertToDTO(product));
    }

    @Override
    public ResponseEntity<ProductDTO> findById(UUID id) {
        return ResponseEntity.ok(ProductDTO.convertToDTO(findProductService.findById(id)));

    }

    @Override
    public ResponseEntity<PagedResponse<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> products = findAllProductService.findAll(pageable).map(ProductDTO::convertToDTO);
        return ResponseEntity.ok(PagedResponse.of(products));
    }

    @Override
    public ResponseEntity<ProductDTO> update(ProductDTO customerDTO) {
        Product product = updateProductService.update(customerDTO);
        return ResponseEntity.ok(ProductDTO.convertToDTO(product));
    }

    @Override
    public ResponseEntity<Void> deleteById(UUID id) {
        deleteProductService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
