package br.com.fiap.controlepedidos.common;

import br.com.fiap.controlepedidos.adapter.rest.dto.RespostaErro;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MENSAGEM_GENERICA = "Um erro aconteceu. Por favor tente novamente.";

    @ExceptionHandler({IllegalArgumentException.class, RegistroExistenteException.class})
    public ResponseEntity<RespostaErro> handle400Requests(Exception exception, WebRequest request) {
        return ResponseEntity.badRequest().body(new RespostaErro(exception.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler({RegistroNaoEncontradoException.class})
    public ResponseEntity<RespostaErro> handle404Requests(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new RespostaErro(exception.getMessage(),
                LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        return ResponseEntity.badRequest().body(new RespostaErro(erros, LocalDateTime.now()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RespostaErro> handleGenericException(Exception exception, WebRequest request) {
        return ResponseEntity.internalServerError().body(new RespostaErro(MENSAGEM_GENERICA,
                LocalDateTime.now()));
    }
}
