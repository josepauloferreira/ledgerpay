package io.github.josepauloferreira.ledgerpay.domain.exception;

public class InvalidMoneyMovementAmountException extends DomainException {
  public InvalidMoneyMovementAmountException(String message) {
    super(message);
  }
}
