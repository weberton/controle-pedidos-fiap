package br.com.fiap.controlepedidos.adapters.driver.apirest.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(HealthCheckApi.BASE_URL)
public interface HealthCheckApi {
    String BASE_URL = "/api/v1/health";

    @GetMapping
    ResponseEntity<String> health();
}