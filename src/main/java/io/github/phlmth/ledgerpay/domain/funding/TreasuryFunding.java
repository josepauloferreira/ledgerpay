package io.github.phlmth.ledgerpay.domain.funding;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;

public class TreasuryFunding {
  public void execute(Wallet treasury, Wallet destination, Money amount) {

    if (amount.equals(Money.of("0.00"))) {
      throw new IllegalArgumentException();
    }

    treasury.debit(amount);
    destination.credit(amount);
  }
}
