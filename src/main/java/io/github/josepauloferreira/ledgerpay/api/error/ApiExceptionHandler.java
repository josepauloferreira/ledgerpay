package io.github.josepauloferreira.ledgerpay.api.error;

import io.github.josepauloferreira.ledgerpay.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("domain_error", "The requested operation violates a domain rule."));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException exception) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("bad_request", "Request validation failed."));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(
      ResponseStatusException exception) {

    if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse("not_found", "Wallet not found."));
    }

    return ResponseEntity.status(exception.getStatusCode())
        .body(new ErrorResponse("error", "Request failed."));
  }
}
