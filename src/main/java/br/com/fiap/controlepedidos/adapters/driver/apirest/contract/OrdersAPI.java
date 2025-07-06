package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.OrderDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.UpdateOrderStatusDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(OrdersAPI.BASE_URL)
public interface OrdersAPI {
    String BASE_URL = "/api/v1/orders";

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAll(Pageable pageable);

    @GetMapping(value = "/getAllInPreparation", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAllInPreparation(Pageable pageable);

    @GetMapping(value = "/getAllReady", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAllReady(Pageable pageable);

    @GetMapping(value = "/getAllDone", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAllDone(Pageable pageable);

    @GetMapping(value = "/getAllToPrepare", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAllToPrepare(Pageable pageable);

    @PutMapping(value = "/startPreparation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDTO> startPreparation(@RequestBody @Valid UpdateOrderStatusDTO order);

    @PutMapping(value = "/informReady", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDTO> informReady(@RequestBody @Valid UpdateOrderStatusDTO order);

    @PutMapping(value = "/informDone", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderDTO> informDone(@RequestBody @Valid UpdateOrderStatusDTO order);

    @GetMapping(value = "/getAllInProcess", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedResponse<OrderDTO>> getAllInProcess(Pageable pageable);
}