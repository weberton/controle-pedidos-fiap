package br.com.fiap.controlepedidos.adapter.rest.dto;

import java.time.LocalDateTime;

public record RespostaErro(String mensagem,
                           LocalDateTime timeStamp) {
}
