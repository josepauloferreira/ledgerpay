package io.github.phlmth.ledgerpay.domain.transfer;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;

public class PeerTransfer {

  public void execute(Wallet source, Wallet destination, Money amount) {
    if (source.id().equals(destination.id())) {
      throw new IllegalArgumentException();
    }

    source.debit(amount);
    destination.credit(amount);
  }
}
