package io.github.phlmth.ledgerpay.domain.exception;

public class InvalidMoneyMovementAmountException extends DomainException {
  public InvalidMoneyMovementAmountException(String message) {
    super(message);
  }
}
