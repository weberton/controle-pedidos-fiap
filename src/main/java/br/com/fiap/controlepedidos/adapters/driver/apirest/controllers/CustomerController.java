package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.CustomerApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.CustomerDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.core.application.ports.CustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.CreateCustomerService;
import br.com.fiap.controlepedidos.core.application.services.customer.DeleteCustomerByIdService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindAllCustomersService;
import br.com.fiap.controlepedidos.core.application.services.customer.FindCustomerByCPFService;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CustomerController implements CustomerApi {


    private final CreateCustomerService createCustomerService;
    private final FindAllCustomersService findAllCustomerService;
    private final FindCustomerByCPFService findAllCustomerByCPFService;
    private final DeleteCustomerByIdService deleteCustomerByIdService;

    public CustomerController(CreateCustomerService createCustomerService,
                              FindAllCustomersService findAllCustomerService,
                              FindCustomerByCPFService findAllCustomerByCPFService,
                              DeleteCustomerByIdService deleteCustomerByIdService) {
        this.createCustomerService = createCustomerService;
        this.findAllCustomerService = findAllCustomerService;
        this.findAllCustomerByCPFService = findAllCustomerByCPFService;
        this.deleteCustomerByIdService = deleteCustomerByIdService;
    }

    @Override
    public ResponseEntity<CustomerDTO> create(final CustomerDTO clienteDTO) {
        Customer clienteSalvo = this.createCustomerService.createCustomer(clienteDTO.convertToModel());
        return new ResponseEntity<>(CustomerDTO.convertToDTO(clienteSalvo), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CustomerDTO> findByCPF(final String cpf) {
        Customer clienteEncontrado = this.findAllCustomerByCPFService.findByCPF(cpf);
        return ResponseEntity.ok(CustomerDTO.convertToDTO(clienteEncontrado));
    }

    @Override
    public ResponseEntity<PagedResponse<CustomerDTO>> findAll(final Pageable pageable) {
        Page<CustomerDTO> result = this.findAllCustomerService.findAll(pageable).map(CustomerDTO::convertToDTO);
        return ResponseEntity.ok(PagedResponse.of(result));
    }

    @Override
    public ResponseEntity<Void> deleteById(final UUID id) {
        this.deleteCustomerByIdService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
