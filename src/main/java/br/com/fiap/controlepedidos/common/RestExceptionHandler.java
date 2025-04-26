package br.com.fiap.controlepedidos.common;

import br.com.fiap.controlepedidos.adapter.rest.dto.RespostaErro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MENSAGEM_GENERICA = "Um erro aconteceu. Por favor tente novamente.";

    @ExceptionHandler({IllegalArgumentException.class, RegistroExistenteException.class})
    public ResponseEntity<RespostaErro> handle400Requests(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new RespostaErro(exception.getMessage(),
                LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RegistroNaoEncontradoException.class})
    public ResponseEntity<RespostaErro> handle404Requests(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new RespostaErro(exception.getMessage(),
                LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RespostaErro> handleGenericException(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new RespostaErro(MENSAGEM_GENERICA,
                LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
