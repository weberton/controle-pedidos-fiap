package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.ProductApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.exceptions.RestExceptionHandler;
import br.com.fiap.controlepedidos.core.application.services.product.CreateProductService;
import br.com.fiap.controlepedidos.core.application.services.product.FindProductByIdService;
import br.com.fiap.controlepedidos.core.application.services.product.UpdateProductService;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CreateProductService createService;

    @Mock
    private FindProductByIdService findByIdService;

    @Mock
    private UpdateProductService updateService;

    @InjectMocks
    private ProductController productController;

    private UUID productId;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
        productId = UUID.randomUUID();
        product = new Product(productId, "Produto Teste", 1999,
                Category.LANCHE, "Hamburguer", true, "");

        productDTO = ProductDTO.convertToDTO(product);
    }

    @Test
    void testCreateProduct() throws Exception {
        Mockito.when(createService.create(any())).thenReturn(product);

        mockMvc.perform(post(ProductApi.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(findByIdService.findById(productId)).thenReturn(product);

        mockMvc.perform(get(ProductApi.BASE_URL + "/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()));
    }

    @Test
    void testFindById_whenProductDoesNotExist_returns404() throws Exception {
        Mockito.when(findByIdService.findById(productId)).thenThrow(RecordNotFoundException.class);

        mockMvc.perform(get(ProductApi.BASE_URL + "/" + productId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct() throws Exception {
        Mockito.when(updateService.update(any(), any())).thenReturn(product);

        mockMvc.perform(put(ProductApi.BASE_URL + "/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

}
