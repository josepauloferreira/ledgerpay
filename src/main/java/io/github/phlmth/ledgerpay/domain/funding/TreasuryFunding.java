package io.github.phlmth.ledgerpay.domain.funding;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;

public class TreasuryFunding {
  public void execute(Wallet treasury, Wallet destination, Money amount) {
    treasury.debit(amount);
    destination.credit(amount);
  }
}
