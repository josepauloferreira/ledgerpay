package io.github.phlmth.ledgerpay.domain.exception;

public class InvalidWalletBalanceException extends DomainException {
  public InvalidWalletBalanceException(String message) {
    super(message);
  }
}
