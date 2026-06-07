package io.github.phlmth.ledgerpay.domain.exception;

public class InsufficientBalanceException extends DomainException {
  public InsufficientBalanceException(String message) {
    super(message);
  }
}
