package io.github.phlmth.ledgerpay.domain.transfer;

import io.github.phlmth.ledgerpay.domain.exception.SameWalletTransferException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.util.Objects;

public class PeerTransfer {

  public void execute(Wallet source, Wallet destination, Money amount) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(destination);
    Objects.requireNonNull(amount);

    if (source.id().equals(destination.id())) {
      throw new SameWalletTransferException("same wallet transfer");
    }

    source.debit(amount);
    destination.credit(amount);
  }
}
