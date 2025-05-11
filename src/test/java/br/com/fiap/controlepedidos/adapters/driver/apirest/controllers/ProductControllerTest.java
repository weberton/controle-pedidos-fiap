package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.ProductApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ProductDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.exceptions.RestExceptionHandler;
import br.com.fiap.controlepedidos.core.application.services.product.ICreate;
import br.com.fiap.controlepedidos.core.application.services.product.IFindAll;
import br.com.fiap.controlepedidos.core.application.services.product.IFindById;
import br.com.fiap.controlepedidos.core.application.services.product.IUpdate;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
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
    private ICreate createService;

    @Mock
    private IFindById findByIdService;

    @Mock
    private IFindAll findAllService;

    @Mock
    private IUpdate updateService;

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
        product = new Product(productId, "Produto Teste", new BigDecimal("19.99"),
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

//    @Test
//    void testFindAll() throws Exception {
//        Page<Product> page = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
//        Mockito.when(findAllService.findAll(any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get(ProductApi.BASE_URL + "?nome=Produto Teste"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].id").value(productId.toString()));
//    }

    @Test
    void testUpdateProduct() throws Exception {
        Mockito.when(updateService.update(any(), any())).thenReturn(product);

        mockMvc.perform(put(ProductApi.BASE_URL + "/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

//    @Test
//    void testDeleteProduct() throws Exception {
//
//        this.testCreateProduct();
//
//        mockMvc.perform(delete(ProductApi.BASE_URL + "/" + productId))
//                .andExpect(status().isNoContent());
//    }

}
