package br.com.fiap.controlepedidos.adapter.rest;

import br.com.fiap.controlepedidos.adapters.driver.apirest.controllers.CartsController;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.Category;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CartAssociateCustomerRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.CreateItemRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.UpdateItemQuantityRequest;
import br.com.fiap.controlepedidos.adapters.driver.apirest.exceptions.RestExceptionHandler;
import br.com.fiap.controlepedidos.core.application.services.carts.CartAssociateCustomerService;
import br.com.fiap.controlepedidos.core.application.services.carts.CreateUpdateCartService;
import br.com.fiap.controlepedidos.core.application.services.carts.DeleteCartService;
import br.com.fiap.controlepedidos.core.application.services.carts.FindCartService;
import br.com.fiap.controlepedidos.core.domain.entities.Cart;
import br.com.fiap.controlepedidos.core.domain.entities.CartItem;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import br.com.fiap.controlepedidos.core.domain.entities.Product;
import br.com.fiap.controlepedidos.core.domain.validations.CartAlreadyAssociatedException;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class CartsControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private CreateUpdateCartService createUpdateCartService;
    @Mock
    private FindCartService findCartService;
    @Mock
    private CartAssociateCustomerService associateCustomerService;
    @Mock
    private DeleteCartService deleteCartService;
    @InjectMocks
    private CartsController cartsController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartsController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void createCart_whenQuantityIsLessThan1_returns400() throws Exception {
        var productId = UUID.randomUUID().toString();
        var quantity = 0;

        CreateItemRequest createItemRequest = new CreateItemRequest(productId, quantity);
        mockMvc.perform(post(CartsController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(createItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(CreateItemRequest.INVALID_QUANTITY)));
    }

    @Test
    void createCart_whenProductIdIsNull_returns400() throws Exception {
        var quantity = 0;

        CreateItemRequest createItemRequest = new CreateItemRequest(null, quantity);
        mockMvc.perform(post(CartsController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(createItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(CreateItemRequest.NULL_PRODUCT_ID)));
    }


    @Test
    void createCart_whenProductDoesNoExist_returns400() throws Exception {

        var productId = UUID.randomUUID().toString();
        var quantity = 2;

        when(createUpdateCartService.create(any())).thenThrow(RecordNotFoundException.class);

        CreateItemRequest createItemRequest = new CreateItemRequest(productId, quantity);
        mockMvc.perform(post(CartsController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(createItemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void findById_whenCartExists_returnsCart() throws Exception {
        //Given
        var cart = new Cart();
        cart.setId(UUID.randomUUID());

        //When
        when(findCartService.findById(any())).thenReturn(cart);
        mockMvc.perform(get(CartsController.BASE_URL + "/" + cart.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.cartId").value(cart.getId().toString()));

    }

    @Test
    void findById_whenCartDoesNotExist_returns404() throws Exception {
        //Given
        var cart = new Cart();
        cart.setId(UUID.randomUUID());

        //When/Then
        when(findCartService.findById(any())).thenThrow(RecordNotFoundException.class);
        mockMvc.perform(get(CartsController.BASE_URL + "/" + cart.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addItem_whenAddItem_returnsCarts() throws Exception {
        //Given
        var cartId = UUID.randomUUID();
        var productId = UUID.randomUUID().toString();
        var itemRequest = new CreateItemRequest(productId, 2);

        var cart = new Cart();
        cart.setId(cartId);
        cart.addItem(itemRequest.toDomain());

        //When/Then
        when(createUpdateCartService.addItem(any(), any())).thenReturn(cart);
        mockMvc.perform(post(CartsController.BASE_URL + "/" + cart.getId() + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(itemRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.cartId").value(cart.getId().toString()));
    }

    @Test
    void associateCustomer_whenCartIsNotAssociated_associatedCartsToCustomer() throws Exception {
        //Given
        var cartId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var customer = Customer.builder().id(customerId).build();
        var product = Product.builder()
                .description("Produto 1")
                .category(Category.SOBREMESA)
                .id(UUID.randomUUID())
                .build();

        CartItem item = CartItem.builder()
                .priceCents(1000)
                .quantity(1)
                .product(product)
                .build();

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.addItem(item);
        cart.setCustomer(customer);

        CartAssociateCustomerRequest cartAssociateCustomerRequest = new CartAssociateCustomerRequest(customerId);

        //When/Then
        when(associateCustomerService.associateCustomer(cartId, customerId)).thenReturn(cart);

        mockMvc.perform(post(CartsController.BASE_URL + "/" + cartId + "/identify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(cartAssociateCustomerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()));
    }


    @Test
    void associateCustomer_whenCartsIsAlreadyAssociated_returnsConflict() throws Exception {
        //Given
        var cartId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        CartAssociateCustomerRequest cartAssociateCustomerRequest = new CartAssociateCustomerRequest(customerId);

        //When/Then
        when(associateCustomerService.associateCustomer(any(), any())).thenThrow(CartAlreadyAssociatedException.class);

        mockMvc.perform(post(CartsController.BASE_URL + "/" + cartId + "/identify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(cartAssociateCustomerRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void delete_deleteCartWhenExists() throws Exception {
        //Given
        var cartId = UUID.randomUUID();

        //When
        doNothing().when(deleteCartService).deleteById(cartId);

        //Then/When
        mockMvc.perform(delete(CartsController.BASE_URL + "/" + cartId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateItemQuantity_whenQuantityIs0_shouldReturn200() throws Exception {
        //Given
        var cartId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var newQuantity = 0;
        var updateItemQuantityRequest = new UpdateItemQuantityRequest(newQuantity);

        Cart cart = new Cart();
        cart.setId(cartId);

        //When
        when(createUpdateCartService.updateQuantity(cartId, itemId, newQuantity)).thenReturn(cart);

        //Then
        mockMvc.perform(patch(CartsController.BASE_URL + "/" + cartId + "/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateItemQuantityRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateItemQuantity_whenQuantityLessThan0_shouldReturn400() throws Exception {
        var cartId = UUID.randomUUID();
        var itemId = UUID.randomUUID();
        var updateItemQuantityRequest = new UpdateItemQuantityRequest(-1);

        mockMvc.perform(patch(CartsController.BASE_URL + "/" + cartId + "/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateItemQuantityRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content()
                        .string(CoreMatchers.containsString(UpdateItemQuantityRequest.INVALID_QUANTITY)));
    }

    private String toJson(Object createItemRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(createItemRequest);
    }

}
