package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.OrdersAPI;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.OrderDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.in.UpdateOrderStatusDTO;
import br.com.fiap.controlepedidos.core.application.services.order.*;
import br.com.fiap.controlepedidos.core.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
public class OrdersController implements OrdersAPI {

    private final FinishOrderService finishOrderService;
    private final StartOrderPreparation startOrderPreparation;
    private final FinishOrderPreparationService finishOrderPreparationService;
    private final GetAllOrdersService getAllOrdersService;
    private final GetAllOrdersInPrepService getAllOrdersInPrepService;
    private final GetAllOrdersReadyService getAllOrdersReadyService;
    private final GetAllOrdersDoneService getAllOrdersDoneService;
    private final GetAllOrdersReadyToPrepareService getAllOrdersReadyToPrepareService;


    public OrdersController(FinishOrderService finishOrderService, StartOrderPreparation startOrderPreparation, FinishOrderPreparationService finishOrderPreparationService, GetAllOrdersService getAllOrdersService, GetAllOrdersInPrepService getAllOrdersInPrepService, GetAllOrdersReadyService getAllOrdersReadyService, GetAllOrdersDoneService getAllOrdersDoneService, GetAllOrdersReadyToPrepareService getAllOrdersReadyToPrepareService) {
        this.finishOrderService = finishOrderService;
        this.startOrderPreparation = startOrderPreparation;
        this.finishOrderPreparationService = finishOrderPreparationService;
        this.getAllOrdersService = getAllOrdersService;
        this.getAllOrdersInPrepService = getAllOrdersInPrepService;
        this.getAllOrdersReadyService = getAllOrdersReadyService;
        this.getAllOrdersDoneService = getAllOrdersDoneService;
        this.getAllOrdersReadyToPrepareService = getAllOrdersReadyToPrepareService;
    }

    @Override
    public ResponseEntity<PagedResponse<OrderDTO>> getAll(Pageable pageable) {

        try {
            Page<Order> page = getAllOrdersService.getAll(pageable);

            Page<OrderDTO> dtoPage = page.map(order ->
                    OrderDTO.convertToDTO(order,
                            order.getCustomer() != null ? order.getCustomer() : null
                    )
            );

            return ResponseEntity.ok(PagedResponse.of(dtoPage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @Override
    public ResponseEntity<PagedResponse<OrderDTO>> getAllInPreparation(Pageable pageable) {
        try {
            Page<Order> page = getAllOrdersInPrepService.getAll(pageable);

            Page<OrderDTO> dtoPage = page.map(order ->
                    OrderDTO.convertToDTO(order,
                            order.getCustomer() != null ? order.getCustomer() : null
                    )
            );

            return ResponseEntity.ok(PagedResponse.of(dtoPage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<PagedResponse<OrderDTO>> getAllReady(Pageable pageable) {
        try {
            Page<Order> page = getAllOrdersReadyService.getAll(pageable);

            Page<OrderDTO> dtoPage = page.map(order ->
                    OrderDTO.convertToDTO(order,
                            order.getCustomer() != null ? order.getCustomer() : null
                    )
            );

            return ResponseEntity.ok(PagedResponse.of(dtoPage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<PagedResponse<OrderDTO>> getAllDone(Pageable pageable) {
        try {
            Page<Order> page = getAllOrdersDoneService.getAll(pageable);

            Page<OrderDTO> dtoPage = page.map(order ->
                    OrderDTO.convertToDTO(order,
                            order.getCustomer() != null ? order.getCustomer() : null
                    )
            );

            return ResponseEntity.ok(PagedResponse.of(dtoPage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<PagedResponse<OrderDTO>> getAllToPrepare(Pageable pageable) {
        try {
            Page<Order> page = getAllOrdersReadyToPrepareService.getAll(pageable);

            Page<OrderDTO> dtoPage = page.map(order ->
                    OrderDTO.convertToDTO(order,
                            order.getCustomer() != null ? order.getCustomer() : null
                    )
            );

            return ResponseEntity.ok(PagedResponse.of(dtoPage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<OrderDTO> startPreparation(UpdateOrderStatusDTO order) throws Exception {
        Order orderUpdated = startOrderPreparation.perform(order.orderId());
        return new ResponseEntity<>(OrderDTO.convertToDTO(orderUpdated, orderUpdated.getCustomer()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDTO> informReady(UpdateOrderStatusDTO order) throws Exception {
        Order orderUpdated = finishOrderPreparationService.perform(order.orderId());
        return new ResponseEntity<>(OrderDTO.convertToDTO(orderUpdated, orderUpdated.getCustomer()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDTO> informDone(UpdateOrderStatusDTO order) throws Exception {
        Order orderUpdated = finishOrderService.perform(order.orderId());
        return new ResponseEntity<>(OrderDTO.convertToDTO(orderUpdated, orderUpdated.getCustomer()), HttpStatus.OK);
    }

}