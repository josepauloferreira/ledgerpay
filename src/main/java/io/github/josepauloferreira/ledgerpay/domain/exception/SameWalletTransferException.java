package io.github.josepauloferreira.ledgerpay.domain.exception;

public class SameWalletTransferException extends DomainException {
  public SameWalletTransferException(String message) {
    super(message);
  }
}
