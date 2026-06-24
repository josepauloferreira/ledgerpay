package io.github.josepauloferreira.ledgerpay.api.error;

import io.github.josepauloferreira.ledgerpay.domain.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Void> handleDomainException(DomainException exception) {
    return ResponseEntity.badRequest().build();
  }
}
