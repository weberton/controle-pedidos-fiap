package br.com.fiap.controlepedidos.adapters.driver.apirest.controllers;

import br.com.fiap.controlepedidos.adapters.driver.apirest.contract.CustomerApi;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.CustomerDTO;
import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.PagedResponse;
import br.com.fiap.controlepedidos.core.application.ports.ICustomerRepository;
import br.com.fiap.controlepedidos.core.application.services.customer.ICreateCustomer;
import br.com.fiap.controlepedidos.core.application.services.customer.IFindAllCustomers;
import br.com.fiap.controlepedidos.core.application.services.customer.IFindCustomerByCPF;
import br.com.fiap.controlepedidos.core.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CustomerController implements CustomerApi {

    private final ICreateCustomer _createCustomerService;
    private final IFindAllCustomers _findAllCustomerService;
    private final IFindCustomerByCPF _findAllCustomerByCPFService;
    private final ICustomerRepository _deleteCustomerByIdService;

    public CustomerController(ICreateCustomer createCustomerService, IFindAllCustomers findAllCustomerService, IFindCustomerByCPF findAllCustomerByCPFService, ICustomerRepository deleteCustomerByIdService) {
        _createCustomerService = createCustomerService;
        _findAllCustomerService = findAllCustomerService;
        _findAllCustomerByCPFService = findAllCustomerByCPFService;
        _deleteCustomerByIdService = deleteCustomerByIdService;
    }

    @Override
    public ResponseEntity<CustomerDTO> create(final CustomerDTO clienteDTO) {
        Customer clienteSalvo = this._createCustomerService.createCustomer(clienteDTO.convertToModel());
        return new ResponseEntity<>(CustomerDTO.convertToDTO(clienteSalvo),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CustomerDTO> findByCPF(final String cpf) {
        Customer clienteEncontraddo = this._findAllCustomerByCPFService.findByCPF(cpf);
        return ResponseEntity.ok(CustomerDTO.convertToDTO(clienteEncontraddo));
    }

    @Override
    public ResponseEntity<PagedResponse<CustomerDTO>> findAll(final Pageable pageable) {
        Page<CustomerDTO> result = this._findAllCustomerService.findAll(pageable).map(CustomerDTO::convertToDTO);
        return ResponseEntity.ok(PagedResponse.of(result));
    }

    @Override
    public ResponseEntity<Void> deleteById(@PathVariable final UUID id) {
        this._deleteCustomerByIdService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
