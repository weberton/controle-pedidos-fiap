package br.com.fiap.controlepedidos.adapters.driver.apirest.exceptions;

import br.com.fiap.controlepedidos.adapters.driver.apirest.dto.ErrorResponse;
import br.com.fiap.controlepedidos.core.domain.validations.ExistentRecordException;
import br.com.fiap.controlepedidos.core.domain.validations.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String GENERIC_MESSAGE = "Um erro aconteceu. Por favor tente novamente.";

    @ExceptionHandler({IllegalArgumentException.class, ExistentRecordException.class})
    public ResponseEntity<ErrorResponse> handle400Requests(Exception exception, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler({RecordNotFoundException.class})
    public ResponseEntity<ErrorResponse> handle404Requests(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),
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

        return ResponseEntity.badRequest().body(new ErrorResponse(erros, LocalDateTime.now()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, WebRequest request) {
        logger.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().body(new ErrorResponse(GENERIC_MESSAGE,
                LocalDateTime.now()));
    }
}
