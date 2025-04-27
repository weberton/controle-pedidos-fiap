package br.com.fiap.controlepedidos.adapters.driver.apirest.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message,
                            LocalDateTime timeStamp) {
}
